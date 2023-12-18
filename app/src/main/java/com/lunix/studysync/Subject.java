package com.lunix.studysync;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Subject {
    String course, date;

    public Subject() {
    }

    public Subject(String course, String date) {
        this.course = course;
        this.date = date;
    }

    public String getCourse() {
        return course;
    }

    public String getDate() {
        return date;
    }
}
