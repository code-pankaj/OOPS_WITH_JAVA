package com.course.main;

import com.course.exception.CourseFullException;
import com.course.exception.CourseNotFoundException;
import com.course.exception.DuplicateEnrollmentException;
import com.course.model.Course;
import com.course.model.Student;
import com.course.service.CourseService;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		CourseService courseService = new CourseService();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n=== Online Course Enrollment System ===");
			System.out.println("1. Add Course");
			System.out.println("2. Enroll Student");
			System.out.println("3. View Courses");
			System.out.println("4. Exit");
			System.out.print("Enter choice: ");

			int choice;
			try {
				choice = Integer.parseInt(scanner.nextLine().trim());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number.");
				continue;
			}

			if (choice == 1) {
				try {
					System.out.print("Enter course ID: ");
					int courseId = Integer.parseInt(scanner.nextLine().trim());

					System.out.print("Enter course name: ");
					String courseName = scanner.nextLine().trim();

					System.out.print("Enter max seats: ");
					int maxSeats = Integer.parseInt(scanner.nextLine().trim());

					Course course = new Course(courseId, courseName, maxSeats, 0);
					courseService.addCourse(course);
					System.out.println("Course added successfully.");
				} catch (NumberFormatException e) {
					System.out.println("Invalid numeric input.");
				}
			} else if (choice == 2) {
				try {
					System.out.print("Enter course ID: ");
					int courseId = Integer.parseInt(scanner.nextLine().trim());

					System.out.print("Enter student ID: ");
					String studentId = scanner.nextLine().trim();

					System.out.print("Enter student name: ");
					String studentName = scanner.nextLine().trim();

					Student student = new Student(studentId, studentName);
					courseService.enrollStudent(courseId, student);
					System.out.println("Student enrolled successfully.");
				} catch (NumberFormatException e) {
					System.out.println("Invalid numeric input.");
				} catch (CourseNotFoundException | CourseFullException | DuplicateEnrollmentException e) {
					System.out.println(e.getMessage());
				}
			} else if (choice == 3) {
				courseService.viewCourses();
			} else if (choice == 4) {
				System.out.println("Exiting program.");
				break;
			} else {
				System.out.println("Invalid choice.");
			}
		}

		scanner.close();
	}
}
