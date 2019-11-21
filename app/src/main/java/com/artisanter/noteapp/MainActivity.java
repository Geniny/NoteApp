package com.artisanter.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> notes;
    ArrayList<Note> filteredNotes;
    ListView notesView;
    EditText searchBar;
    NoteAdapter adapter;
    NoteDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = App.getInstance().getDao();
        notesView = findViewById(R.id.notes);
        notesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("uid", Objects.requireNonNull(adapter.getItem(position)).uid);
                startActivity(intent);
            }
        });

        notesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                dao.delete(adapter.getItem(position));
                adapter.remove(adapter.getItem(position));
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Заметка удалена", Toast.LENGTH_SHORT)
                .show();
                return true;
            }
        });

        searchBar = findViewById(R.id.search);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        init();
        notes = new ArrayList<>(dao.getAll());
        filteredNotes = new ArrayList<>(notes);
        Collections.sort(notes, comparator);
        adapter = new NoteAdapter(this, R.layout.list_item, filteredNotes);
        notesView.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        notes = new ArrayList<>(dao.getAll());
        search();
        super.onResume();
    }
    void search(){
        filteredNotes.clear();
        if(searchBar.getText().toString().isEmpty()){
            filteredNotes.addAll(notes);
            setSorted();
            return;
        }
        List<String> tags = Arrays.asList(searchBar.getText().toString().split(", "));
        for(Note note:notes){
            List<String> noteTags = Arrays.asList(note.tags.split(", "));
            if(noteTags.containsAll(tags))
                filteredNotes.add(note);
        }
        setSorted();
    }
    void init() {
        dao.deleteAll();
        for(int i = 0; i<= 3;i++){
            Note note = new Note();
            note.content = "ccc";
            note.title = "#" + i;
            note.tags = "tag" + (i+1) +", tag" + i;
            dao.insert(note);
        }
    }

    public void addClick(View view) {
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        startActivity(intent);
    }

    public void sortClick(View view) {
        if(comparator == dateComparator)
            comparator = titleComparator;
        else
            comparator = dateComparator;
        setSorted();
    }

    private void setSorted(){
        Collections.sort(filteredNotes, comparator);
        adapter.notifyDataSetChanged();

    }
    Comparator<Note> titleComparator = new Comparator<Note>() {
        @Override
        public int compare(Note n1, Note n2) {
            return n1.title.compareTo(n2.title);
        }
    };
    Comparator<Note> dateComparator = new Comparator<Note>() {
        @Override
        public int compare(Note n1, Note n2) {
            return n2.title.compareTo(n1.title);
        }
    };
    Comparator<Note> comparator = dateComparator;

    public void onClear(View view) {
        searchBar.setText("");
        search();
    }
}
