package com.artisanter.noteapp;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

class NoteAdapter extends ArrayAdapter<Note> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Note> notes;

    public NoteAdapter(Context context, int resource, ArrayList<Note> notes) {
        super(context, resource, notes);
        this.notes = notes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null)
                view = inflater.inflate(this.layout, parent, false);

        TextView title = (TextView) view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        TextView tags = (TextView) view.findViewById(R.id.tags);
        TextView date = (TextView) view.findViewById(R.id.date);


        Note note = notes.get(position);

        title.setText(note.getTitle());
        content.setText(note.getContent());
        StringBuilder sb = new StringBuilder();
        for(String tag: note.getTags()){
            sb.append(" #").append(tag);
        }
        tags.setText(sb.toString());
        date.setText(note.getDate().toString());

        return view;
    }
}
