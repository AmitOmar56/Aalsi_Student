package com.example.user.aalsi_student.model;

/**
 * Created by user on 12/4/2017.
 */

public class InstituteLayout {
    private String instituteName;
    private String courseName;
    private int institute_price;
    private double rating;
    private int offer;
    private int institute_id;
    private String institute_course_image;

    public InstituteLayout(String instituteName, String courseName, int institute_price, double rating, int offer, int institute_id,String institute_course_image) {
        this.instituteName = instituteName;
        this.courseName = courseName;
        this.institute_price = institute_price;
        this.rating = rating;
        this.offer = offer;
        this.institute_id = institute_id;
        this.institute_course_image=institute_course_image;
    }

    public String getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(String instituteName) {
        this.instituteName = instituteName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getInstitute_price() {
        return institute_price;
    }

    public void setInstitute_price(int institute_price) {
        this.institute_price = institute_price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getOffer() {
        return offer;
    }

    public void setOffer(int offer) {
        this.offer = offer;
    }

    public int getInstitute_id() {
        return institute_id;
    }

    public void setInstitute_id(int institute_id) {
        this.institute_id = institute_id;
    }

    public String getInstitute_course_image() {
        return institute_course_image;
    }

    public void setInstitute_course_image(String institute_course_image) {
        this.institute_course_image = institute_course_image;
    }
}
