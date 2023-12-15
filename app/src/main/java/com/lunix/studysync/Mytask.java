package com.lunix.studysync;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Mytask {
    private String name;
    private String course;
    private String date;

    public Mytask() {
    }

    public Mytask(String name, String course, String date) {
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
}
