package com.lunix.studysync;


import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Exam {
    String name, course, date;

    public Exam() {
    }

    public Exam(String name, String course, String date) {
        this.name = name;
        this.course = course;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getDate() {
        return date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
