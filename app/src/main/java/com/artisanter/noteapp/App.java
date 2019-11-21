package com.artisanter.noteapp;

import android.app.Application;
import android.content.Intent;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private NoteDB database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NoteDB.class, "database")
                .allowMainThreadQueries()
                .build();

        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static App getInstance() {
        return instance;
    }

    public NoteDB getDatabase() {
        return database;
    }

    public NoteDao getDao(){
        return database.noteDao();
    }
}