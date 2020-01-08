package com.geniny.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ArrayList<Note> notes, sortedNotes;
    AbsListView notesView;
    EditText searchBar;
    Adapter adapter;
    NoteDao dao;
    ImageButton sort;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = App.getInstance().getDao();
        notesView = findViewById(R.id.notes);
        sort = findViewById(R.id.sort);
        sort.setBackgroundResource(R.drawable.ic_sort_by_alpha);
        notesView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("uid", Objects.requireNonNull(adapter.getItem(position)).uid);
                intent.putExtra("flag",false);
                startActivity(intent);
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

        notesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchBar.clearFocus();
                InputMethodManager imm = (InputMethodManager) MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(notesView.getWindowToken(), 0);
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search();
            }
        });
        notes = new ArrayList<>(dao.getAll());
        sortedNotes = new ArrayList<>(notes);
        Collections.sort(notes, comparator);
        adapter = new Adapter(this, R.layout.list_item, sortedNotes);
        notesView.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        notes = new ArrayList<>(dao.getAll());
        search();
        super.onResume();
    }
    void search(){
        sortedNotes.clear();
        if(searchBar.getText().toString().isEmpty()){
            sortedNotes.addAll(notes);
            setSorted();
            return;
        }
        List<String> tags = Arrays.asList(searchBar.getText().toString().split(" "));
        for(Note note:notes){
            List<String> noteTags = Arrays.asList(note.tags.split("#"));
            if(noteTags.containsAll(tags))
                sortedNotes.add(note);
        }
        setSorted();
    }

    public void addClick(View view) {

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
        startActivity(intent);
    }

    public void sortClick(View view) {
        if(comparator == dateComparator){
            comparator = titleComparator;
            toast = Toast.makeText(getApplicationContext(), "Cортировка по названию", Toast.LENGTH_SHORT);
            sort.setBackgroundResource(R.drawable.ic_date_range);
            toast.show();
        }
        else {
            comparator = dateComparator;
            toast = Toast.makeText(getApplicationContext(), "Сортировка по дате", Toast.LENGTH_SHORT);
            sort.setBackgroundResource(R.drawable.ic_sort_by_alpha);
            toast.show();
        }
        setSorted();
    }

    private void setSorted(){
        Collections.sort(sortedNotes, comparator);
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
            return n2.date.compareTo(n1.date);
        }
    };
    Comparator<Note> comparator = dateComparator;

    public void onClear(View view) {
        searchBar.clearFocus();
        searchBar.setText("");
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);

        search();
    }
}
