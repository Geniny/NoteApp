package com.artisanter.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    ListView notesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesView = (ListView) findViewById(R.id.notes);
        //NoteContext.Open();
        //notes = NoteContext.GetNotes();
        init();
        NoteAdapter adapter = new NoteAdapter(this, R.layout.list_item, notes);
        notesView.setAdapter(adapter);

    }

    void init() {
        notes = new ArrayList<>();
        Note note = new Note();
        note.setTitle("Заголовок");
        note.setContent("Наполнение");
        note.getTags().add("тег1");
        note.getTags().add("тег2");
        notes.add(note);
    }
}
