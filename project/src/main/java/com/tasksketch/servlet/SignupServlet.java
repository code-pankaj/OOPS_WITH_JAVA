package com.tasksketch.servlet;

import com.tasksketch.dao.UserDAO;
import com.tasksketch.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SignupServlet.java — Handles new user registration.
 *
 * GET  /signup  → Show the signup page (signup.jsp)
 * POST /signup  → Process registration form submission
 *
 * Flow: Request → SignupServlet → UserDAO → Database → signup.jsp or login
 */
@WebServlet("/signup")
public class SignupServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    /**
     * Displays the signup page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If user is already logged in, redirect to dashboard
        if (request.getSession(false) != null &&
            request.getSession().getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }

        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    /**
     * Processes the signup form.
     * Validates input, checks for duplicate email, and creates the user.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate: all fields required
        if (name == null || name.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Validate: passwords must match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Validate: password length
        if (password.length() < 4) {
            request.setAttribute("error", "Password must be at least 4 characters.");
            request.setAttribute("name", name);
            request.setAttribute("email", email);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Check if email is already taken
        if (userDAO.isEmailTaken(email.trim())) {
            request.setAttribute("error", "Email is already registered. Try logging in.");
            request.setAttribute("name", name);
            request.getRequestDispatcher("signup.jsp").forward(request, response);
            return;
        }

        // Create a new User object
        User newUser = new User();
        newUser.setName(name.trim());
        newUser.setEmail(email.trim());
        newUser.setPassword(password);

        // Register the user via DAO
        boolean success = userDAO.registerUser(newUser);

        if (success) {
            // Registration successful → redirect to login with a success message
            request.setAttribute("success", "Account created! Please log in.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");
            request.getRequestDispatcher("signup.jsp").forward(request, response);
        }
    }
}
