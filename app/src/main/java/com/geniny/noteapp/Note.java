package com.geniny.noteapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
class Note {

    @PrimaryKey(autoGenerate = true)
    public long uid = 0;

    Note()
    {
        date = new Date();
    }

    public String title;
    public String tags;
    public String content;

    @TypeConverters({DateConverter.class})
    public Date date;


}
