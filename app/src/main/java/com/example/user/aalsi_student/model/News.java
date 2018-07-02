package com.example.user.aalsi_student.model;

/**
 * Created by user on 11/7/2017.
 */

public class News {
    private String news_name;
    private int news_image;

    public News(String news_name,int news_image){
        this.news_name=news_name;
        this.news_image=news_image;
    }

    public String getNews_name() {
        return news_name;
    }

    public void setNews_name(String news_name) {
        this.news_name = news_name;
    }

    public int getNews_image() {
        return news_image;
    }

    public void setNews_image(int news_image) {
        this.news_image = news_image;
    }
}
