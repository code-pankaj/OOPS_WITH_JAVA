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
 * LoginServlet.java — Handles user login.
 *
 * GET  /login  → Show the login page (login.jsp)
 * POST /login  → Process login form submission
 *
 * Flow: Request → LoginServlet → UserDAO → Database → login.jsp or dashboard
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    /**
     * Displays the login page.
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

        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    /**
     * Processes the login form.
     * Validates credentials and creates a session on success.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input
        if (email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Please fill in all fields.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // Attempt login via DAO
        User user = userDAO.loginUser(email.trim(), password);

        if (user != null) {
            // Login successful — create session and store user
            request.getSession().setAttribute("user", user);
            response.sendRedirect("dashboard");
        } else {
            // Login failed — show error message
            request.setAttribute("error", "Invalid email or password. Please try again.");
            request.setAttribute("email", email); // Preserve email input
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
