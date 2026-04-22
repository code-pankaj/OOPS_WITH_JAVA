package com.tasksketch.model;

import java.sql.Timestamp;

/**
 * Task.java — Java Bean representing a task.
 * Maps to the 'tasks' table in the database.
 */
public class Task {

    private int id;
    private int userId;
    private String title;
    private String status;       // "pending" or "completed"
    private Timestamp createdAt;

    // Default constructor
    public Task() {}

    // Parameterized constructor
    public Task(int id, int userId, String title, String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Helper method to check if the task is completed.
     */
    public boolean isCompleted() {
        return "completed".equals(this.status);
    }
}
