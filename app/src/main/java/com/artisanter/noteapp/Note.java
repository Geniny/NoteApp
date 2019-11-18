package com.artisanter.noteapp;

import java.util.Date;
import java.util.ArrayList;

class Note {
    Note(){
        date = new Date();
        tags = new ArrayList<>();
    }
    private String content;
    private String title;
    private ArrayList<String> tags;
    private Date date;
    private long id = 0;

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    String getTitle() {
        if(title.isEmpty())
            return date.toString();
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    Date getDate() {
        return date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    ArrayList<String> getTags() {
        return tags;
    }

    long getId() {
        return id;
    }

    void setId(long id){
        this.id = id;
    }
}
