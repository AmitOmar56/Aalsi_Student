package com.example.user.aalsi_student.model;

/**
 * Created by user on 10/17/2017.
 */

public class Course {
    private String course_Name;
    private int course_Id;
    private int course_Image;

    public Course(String course_Name, int course_Id, int course_Image) {
        this.course_Name = course_Name;
        this.course_Id = course_Id;
        this.course_Image = course_Image;
    }

    public String getCourse_Name() {
        return course_Name;
    }

    public void setCourse_Name(String course_Name) {
        this.course_Name = course_Name;
    }

    public int getCourse_Id() {
        return course_Id;
    }

    public void setCourse_Id(int course_Id) {
        this.course_Id = course_Id;
    }

    public int getCourse_Image() {
        return course_Image;
    }

    public void setCourse_Image(int course_Image) {
        this.course_Image = course_Image;
    }
}
