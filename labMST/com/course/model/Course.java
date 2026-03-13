package com.course.model;

public class Course {
    private int courseId;
    private String courseName;
    private int maxSeats;
    private int enrolledStudents;

    public Course() {
    }

    public Course(int courseId, String courseName, int maxSeats, int enrolledStudents) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.maxSeats = maxSeats;
        this.enrolledStudents = enrolledStudents;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public int getMaxSeats() {
        return this.maxSeats;
    }

    public int getEnrolledStudents() {
        return this.enrolledStudents;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;

    }

    public void setEnrolledStudents(int enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public void display() {
        System.out.println("CourseId: " + courseId);
        System.out.println("CourseName: " + courseName);
        System.out.println("MaxSeats: " + maxSeats);
        System.out.println("EnrolledStudents: " + enrolledStudents);
    }

}

