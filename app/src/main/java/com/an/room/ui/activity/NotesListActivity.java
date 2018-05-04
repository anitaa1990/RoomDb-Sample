package com.an.room.ui.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.an.room.AppConstants;
import com.an.room.R;
import com.an.room.model.Note;
import com.an.room.repository.NoteRepository;
import com.an.room.ui.adapter.NotesListAdapter;
import com.an.room.util.NavigatorUtils;
import com.an.room.util.RecyclerItemClickListener;

import java.util.List;

public class NotesListActivity extends AppCompatActivity implements View.OnClickListener,
                                                        RecyclerItemClickListener.OnRecyclerViewItemClickListener, AppConstants {


    private TextView emptyView;
    private RecyclerView recyclerView;
    private NotesListAdapter notesListAdapter;
    private FloatingActionButton floatingActionButton;

    private NoteRepository noteRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        noteRepository = new NoteRepository(getApplicationContext());

        recyclerView = findViewById(R.id.task_list);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);

        emptyView = findViewById(R.id.empty_view);

        updateTaskList();
    }

    private void updateTaskList() {
        noteRepository.getTasks().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(notes.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    if (notesListAdapter == null) {
                        notesListAdapter = new NotesListAdapter(notes);
                        recyclerView.setAdapter(notesListAdapter);

                    } else notesListAdapter.addTasks(notes);
                } else updateEmptyView();
            }
        });
    }

    private void updateEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }


    /*
     * New note to be added
     * */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(NotesListActivity.this, AddNoteActivity.class);
        startActivityForResult(intent, ACTIVITY_REQUEST_CODE);
    }


    /*
     * update/delete existing note
     * */
    @Override
    public void onItemClick(View parentView, View childView, int position) {
        Note note = notesListAdapter.getItem(position);
        if(note.isEncrypt()) {
            NavigatorUtils.redirectToPwdScreen(this, note);

        } else {
            NavigatorUtils.redirectToEditTaskScreen(this, note);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if(data.hasExtra(INTENT_TASK)) {
                if(data.hasExtra(INTENT_DELETE)) {
                    noteRepository.deleteTask((Note) data.getSerializableExtra(INTENT_TASK));

                } else {
                    noteRepository.updateTask((Note) data.getSerializableExtra(INTENT_TASK));
                }
            } else {
                String title = data.getStringExtra(INTENT_TITLE);
                String desc = data.getStringExtra(INTENT_DESC);
                String pwd = data.getStringExtra(INTENT_PWD);
                boolean encrypt = data.getBooleanExtra(INTENT_ENCRYPT, false);
                noteRepository.insertTask(title, desc, encrypt, pwd);
            }
            updateTaskList();
        }
    }
}
