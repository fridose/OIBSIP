package com.example.todoapp;

public class Task {
    public int id;
    public int userId;
    public String title;
    public String notes;
    public boolean isCompleted;

    public Task(int id, int userId, String title, String notes, boolean isCompleted) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.notes = notes;
        this.isCompleted = isCompleted;
    }
}