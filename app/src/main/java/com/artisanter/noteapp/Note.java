package com.artisanter.noteapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.ArrayList;

@Entity
class Note {
    Note(){
        date = new Date();
    }
    public String content;
    public String title;
    public String tags;
    @TypeConverters({DateConverter.class})
    public Date date;

    @PrimaryKey(autoGenerate = true)
    public long uid = 0;
}
