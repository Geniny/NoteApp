package com.artisanter.noteapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;

public class NoteActivity extends AppCompatActivity {

    Bundle bundle;
    TextView title;
    TextView content;
    TextView tags;
    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        bundle = getIntent().getExtras();
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        tags = findViewById(R.id.tags);

        if(bundle == null){
            note = new Note();
            return;
        }
        note = App.getInstance().getDao().getById(bundle.getLong("uid"));
        title.setText(note.title);
        content.setText(note.content);
        tags.setText(note.tags);
    }

    public void saveClick(View view) {
        note.title = title.getText().toString();
        if(note.title.isEmpty()){
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            note.title = dateFormat.format(note.date);
        }
        note.content = content.getText().toString();
        note.tags = tags.getText().toString();
        if(bundle == null) {
            App.getInstance().getDao().insert(note);
            Toast.makeText(getApplicationContext(), "Заметка добавлена", Toast.LENGTH_SHORT)
                    .show();
        }
        else {
            App.getInstance().getDao().update(note);
            Toast.makeText(getApplicationContext(), "Заметка сохранена", Toast.LENGTH_SHORT)
                    .show();
        }
        finish();
    }
}
