package com.example.user.aalsi_student.model;

/**
 * Created by user on 11/20/2017.
 */

public class VideoScreen {
    private String discription;
    private String coursename;
    private int video_id;
    private String video;
    private double rating;
    private int view;
    private int likes;

    public VideoScreen(String discription, String coursename, int video_id, String video, double rating, int view, int likes) {
        this.discription = discription;
        this.coursename = coursename;
        this.video_id = video_id;
        this.video = video;
        this.rating = rating;
        this.view = view;
        this.likes = likes;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
