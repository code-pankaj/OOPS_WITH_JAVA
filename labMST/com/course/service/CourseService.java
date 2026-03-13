package com.course.service;

import com.course.exception.CourseFullException;
import com.course.exception.CourseNotFoundException;
import com.course.exception.DuplicateEnrollmentException;
import com.course.model.Course;
import com.course.model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseService {
    private final Map<Integer, Course> courses;
    private final Map<Integer, Set<String>> courseEnrollments;
    private static final String FILE_PATH = "courses.txt";

    public CourseService() {
        this.courses = new LinkedHashMap<>();
        this.courseEnrollments = new HashMap<>();
        loadDataFromFile();
    }

    public void addCourse(Course c) {
        courses.put(c.getCourseId(), c);
        courseEnrollments.putIfAbsent(c.getCourseId(), new HashSet<>());
        String data = "COURSE," + c.getCourseId() + "," + c.getCourseName() + "," + c.getMaxSeats() + "," + c.getEnrolledStudents();
        writeLine(data);
    }

    public void enrollStudent(int courseId, Student s)
            throws CourseNotFoundException, CourseFullException, DuplicateEnrollmentException {
        Course course = findCourseById(courseId);

        if (course == null) {
            throw new CourseNotFoundException("Course not found with id: " + courseId);
        }

        Set<String> enrolledStudentIds = courseEnrollments.computeIfAbsent(courseId, id -> new HashSet<>());
        if (enrolledStudentIds.contains(s.getStudentId())) {
            throw new DuplicateEnrollmentException(
                    "Student " + s.getStudentId() + " is already enrolled in course " + course.getCourseName());
        }

        if (course.getEnrolledStudents() >= course.getMaxSeats()) {
            throw new CourseFullException("No seats available in course: " + course.getCourseName());
        }

        enrolledStudentIds.add(s.getStudentId());
        course.setEnrolledStudents(course.getEnrolledStudents() + 1);

        String enroll = "ENROLL," + courseId + "," + s.getStudentId() + "," + s.getStudentName();
        writeLine(enroll);
    }

    public void viewCourses() {
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return;
        }

        for (Course course : courses.values()) {
            course.display();
            System.out.println("AvailableSeats: " + (course.getMaxSeats() - course.getEnrolledStudents()));
            System.out.println("--------------------");
        }
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }

    private void loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 0) {
                    continue;
                }

                if ("COURSE".equals(parts[0]) && parts.length >= 4) {
                    int courseId = Integer.parseInt(parts[1]);
                    String courseName = parts[2];
                    int maxSeats = Integer.parseInt(parts[3]);
                    Course course = new Course(courseId, courseName, maxSeats, 0);
                    courses.put(courseId, course);
                    courseEnrollments.putIfAbsent(courseId, new HashSet<>());
                }

                if ("ENROLL".equals(parts[0]) && parts.length >= 4) {
                    int courseId = Integer.parseInt(parts[1]);
                    String studentId = parts[2];
                    Course course = courses.get(courseId);
                    if (course == null) {
                        continue;
                    }

                    Set<String> enrolledStudentIds = courseEnrollments.computeIfAbsent(courseId, id -> new HashSet<>());
                    if (enrolledStudentIds.add(studentId)) {
                        course.setEnrolledStudents(course.getEnrolledStudents() + 1);
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Unable to load course data: " + e.getMessage());
        }
    }

    private Course findCourseById(int courseId) {
        return courses.get(courseId);
    }

    private void writeLine(String value) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            bw.write(value);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Unable to write to courses.txt: " + e.getMessage());
        }
    }
}
