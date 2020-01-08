package com.geniny.noteapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class}, version = 3, exportSchema = false)
public abstract class NoteDB extends RoomDatabase {
    public abstract NoteDao noteDao();
}