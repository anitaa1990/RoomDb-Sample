package com.an.room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.an.room.dao.DaoAccess;
import com.an.room.model.Note;


@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();
}
