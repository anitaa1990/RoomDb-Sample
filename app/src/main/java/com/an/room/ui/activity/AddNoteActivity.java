package com.an.room.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.an.room.AppConstants;
import com.an.room.R;
import com.an.room.model.Note;
import com.an.room.util.AppUtils;

public class AddNoteActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener,
                                                                    View.OnTouchListener ,AppConstants {

    private EditText editTitle, editDesc, editPwd;
    private TextView textTime, btnDone, toolbarTitle;
    private AppCompatCheckBox checkBox;
    private ImageView btnDelete;

    private Note note;
    private boolean pwdVisible;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        textTime = findViewById(R.id.text_time);
        toolbarTitle = findViewById(R.id.title);
        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_desc);

        editPwd = findViewById(R.id.edit_pwd);
        editPwd.setOnTouchListener(this);

        checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);

        btnDelete = findViewById(R.id.btn_close);
        btnDelete.setOnClickListener(this);

        btnDone = findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);

        note = (Note) getIntent().getSerializableExtra(INTENT_TASK);
        if(note == null) {
            toolbarTitle.setText(getString(R.string.add_task_title));
            btnDelete.setImageResource(R.drawable.btn_done);
            btnDelete.setTag(R.drawable.btn_done);
            textTime.setText(AppUtils.getFormattedDateString(AppUtils.getCurrentDateTime()));

        } else {
            toolbarTitle.setText(getString(R.string.edit_task_title));
            btnDelete.setImageResource(R.drawable.ic_delete);
            btnDelete.setTag(R.drawable.ic_delete);
            if(note.getTitle() != null && !note.getTitle().isEmpty()) {
                editTitle.setText(note.getTitle());
                editTitle.setSelection(editTitle.getText().length());
            }
            if(note.getDescription() != null && !note.getDescription().isEmpty()) {
                editDesc.setText(note.getDescription());
                editDesc.setSelection(editDesc.getText().length());
            }
            if(note.getCreatedAt() != null) {
                textTime.setText(AppUtils.getFormattedDateString(note.getCreatedAt()));
            }
            if(note.getPassword() != null && !note.getPassword().isEmpty()) {
                editPwd.setText(note.getPassword());
                editPwd.setSelection(editPwd.getText().length());
            }
            checkBox.setChecked(note.isEncrypt());
        }

        AppUtils.openKeyboard(getApplicationContext());
    }



    private void togglePwd() {
        if(!pwdVisible) {
            pwdVisible = Boolean.TRUE;
            editPwd.setTransformationMethod(null);
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pwd).mutate();
            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.line), PorterDuff.Mode.MULTIPLY));
            editPwd.setCompoundDrawablesWithIntrinsicBounds(null,null, drawable, null);

        } else {
            pwdVisible = Boolean.FALSE;
            editPwd.setTransformationMethod(new PasswordTransformationMethod());
            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pwd).mutate();
            drawable.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY));
            editPwd.setCompoundDrawablesWithIntrinsicBounds(null,null, drawable, null);
        }
        editPwd.setSelection(editPwd.length());
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b) {
            editPwd.setVisibility(View.VISIBLE);
            editPwd.setFocusable(true);
        } else {
            editPwd.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        AppUtils.hideKeyboard(this);
        if(view == btnDelete) {

            if((Integer)btnDelete.getTag() == R.drawable.btn_done) {
                setResult(Activity.RESULT_CANCELED);

            } else {
                Intent intent = getIntent();
                intent.putExtra(INTENT_DELETE, true);
                intent.putExtra(INTENT_TASK, note);
                setResult(Activity.RESULT_OK, intent);
            }

            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);

        } else if(view == btnDone) {
            Intent intent = getIntent();
            if(note != null) {
                note.setTitle(editTitle.getText().toString());
                note.setDescription(editDesc.getText().toString());
                note.setEncrypt(checkBox.isChecked());
                note.setPassword(editPwd.getText().toString());
                intent.putExtra(INTENT_TASK, note);

            } else {
                intent.putExtra(INTENT_TITLE, editTitle.getText().toString());
                intent.putExtra(INTENT_DESC, editDesc.getText().toString());
                intent.putExtra(INTENT_ENCRYPT, checkBox.isChecked());
                intent.putExtra(INTENT_PWD, editPwd.getText().toString());
            }
            setResult(Activity.RESULT_OK, intent);
            finish();
            overridePendingTransition(R.anim.stay, R.anim.slide_down);
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int DRAWABLE_RIGHT = 2;

        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(view.getId() == R.id.edit_pwd && event.getRawX() >= (editPwd.getRight() - editPwd.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                togglePwd();
                return true;
            }
        }
        return false;
    }
}
