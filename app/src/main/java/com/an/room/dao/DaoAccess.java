package com.an.room.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.an.room.model.Note;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(Note note);


    @Query("SELECT * FROM Note ORDER BY created_at desc")
    LiveData<List<Note>> fetchAllTasks();


    @Query("SELECT * FROM Note WHERE id =:taskId")
    LiveData<Note> getTask(int taskId);


    @Update
    void updateTask(Note note);


    @Delete
    void deleteTask(Note note);
}
