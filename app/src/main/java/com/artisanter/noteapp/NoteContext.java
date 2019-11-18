package com.artisanter.noteapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

class NoteContext {
    private static SQLiteDatabase db;
    static void Open(){
        if(db != null && db.isOpen())
            return;
        db = SQLiteDatabase.openOrCreateDatabase("app.db", null);
    }
    static void Close(){
        if(db != null && db.isOpen())
            db.close();
    }
    static ArrayList<Note> GetNotes(){
        Cursor cursor = db.rawQuery(
                "SELECT id FROM notes;", null);
        ArrayList<Note> list = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                list.add(GetNote(cursor.getLong(0)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    static ArrayList<Note> GetNotes(String tag){
        Cursor cursor = db.rawQuery(
                "SELECT id FROM tags WHERE name=" + tag + ";", null);
        ArrayList<Note> list = new ArrayList<>();
        if(!cursor.moveToFirst())
            return list;
        long tagId = cursor.getLong(0);
        cursor.close();
        cursor = db.rawQuery(
                "SELECT note_id FROM noteTags WHERE tag_id=" + tagId + ";", null);
        if(cursor.moveToFirst()){
            do{
                list.add(GetNote(cursor.getLong(0)));
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    private static Note GetNote(long id) {
        Cursor noteCursor = db.rawQuery(
                "SELECT title, content, date FROM notes Where id=" + id + ";", null);
        Note note = new Note();
        note.setId(id);
        note.setTitle(noteCursor.getString(0));
        note.setContent(noteCursor.getString(1));
        note.setDate(new Date(noteCursor.getLong(2)));
        noteCursor.close();

        Cursor tagsCursor = db.rawQuery(
                "SELECT tag_id FROM notes Where note_id=" + id + ";", null);
        if(tagsCursor.moveToFirst()){
            do{
                note.getTags().add(tagsCursor.getString(0));
            }
            while(tagsCursor.moveToNext());
        }
        tagsCursor.close();
        return note;
    }
    static void AddNote(Note note){
        if(note.getId() != 0)
            DeleteNote(note.getId());
        ContentValues values = new ContentValues();
        values.put("title", note.getTitle());
        values.put("content", note.getContent());
        values.put("date", note.getDate().getTime());
        long noteId = db.insert("notes", null, values);
        for(String tag: note.getTags()){
            Cursor cursor = db.rawQuery(
                    "SELECT id FROM tags WHERE name=" + tag + ";", null);
            long tagId;
            if(cursor.moveToFirst())
                tagId = cursor.getLong(0);
            else {
                ContentValues tagValues = new ContentValues();
                tagValues.put("name", tag);
                tagId = db.insert("tags", null, tagValues);
            }
            cursor.close();
            ContentValues noteTag = new ContentValues();
            noteTag.put("note_id", noteId);
            noteTag.put("tag_id", tagId);
            db.insert("noteTags", null, noteTag);
        }
    }
    static void DeleteNote(long id){
        db.rawQuery(
                "DELETE FROM notes WHERE id=" + id + ";", null).close();
        db.rawQuery(
                "DELETE FROM noteTagss WHERE note_id=" + id + ";", null).close();

    }
}
