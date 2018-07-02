package com.example.user.aalsi_student.model;

/**
 * Created by user on 11/27/2017.
 */

public class Comment {
    private String name;
    private String comment;
    private String date;

    public Comment(String name,String comment,String date) {
        this.name = name;
        this.comment = comment;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

