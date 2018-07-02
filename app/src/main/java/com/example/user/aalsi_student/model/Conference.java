package com.example.user.aalsi_student.model;

/**
 * Created by user on 10/31/2017.
 */

public class Conference {
    private String topic;
    private String date;
    private String time;

    public Conference(String topic, String date, String time) {
        this.topic = topic;
        this.date = date;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
