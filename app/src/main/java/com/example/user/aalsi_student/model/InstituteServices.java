package com.example.user.aalsi_student.model;

/**
 * Created by user on 10/27/2017.
 */

public class InstituteServices {
    private String institute_name;
    private Double institute_rating;
    private int institute_image;

    public InstituteServices(String institute_name,Double institute_rating,int institute_image){
        this.institute_name=institute_name;
        this.institute_rating=institute_rating;
        this.institute_image=institute_image;
    }

    public String getInstitute_name() {
        return institute_name;
    }

    public void setInstitute_name(String institute_name) {
        this.institute_name = institute_name;
    }

    public Double getInstitute_rating() {
        return institute_rating;
    }

    public void setInstitute_rating(Double institute_rating) {
        this.institute_rating = institute_rating;
    }

    public int getInstitute_image() {
        return institute_image;
    }

    public void setInstitute_image(int institute_image) {
        this.institute_image = institute_image;
    }
}
