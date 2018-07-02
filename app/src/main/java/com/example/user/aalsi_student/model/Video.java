package com.example.user.aalsi_student.model;

/**
 * Created by user on 10/28/2017.
 */

public class Video {
    private String course_name;
    private String teacher_name;
    private String video;
    private double rating;
    private int price;
    private int course;
    private String course_logo;
    private int course_offer;

    public Video(String course_name, String teacher_name, String video, double rating, int price, int course, String course_logo, int course_offer) {
        this.course_name = course_name;
        this.teacher_name = teacher_name;
        this.video = video;
        this.rating = rating;
        this.price = price;
        this.course = course;
        this.course_logo = course_logo;
        this.course_offer = course_offer;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCourse() {
        return course;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public String getCourse_logo() {
        return course_logo;
    }

    public void setCourse_logo(String course_logo) {
        this.course_logo = course_logo;
    }

    public int getCourse_offer() {
        return course_offer;
    }

    public void setCourse_offer(int course_offer) {
        this.course_offer = course_offer;
    }
}