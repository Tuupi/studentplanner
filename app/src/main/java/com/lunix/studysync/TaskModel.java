package com.lunix.studysync;

public class TaskModel {
    String name;
    String course;
    String date;

    public TaskModel(String name, String course, String date) {
        this.name = name;
        this.course = course;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
