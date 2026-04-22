package com.tasksketch.servlet;

import com.tasksketch.dao.TaskDAO;
import com.tasksketch.model.Task;
import com.tasksketch.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AddTaskServlet.java — Handles adding a new task.
 *
 * GET  /addtask  → Show the "Add Task" form (addtask.jsp)
 * POST /addtask  → Process the form and insert a new task
 */
@WebServlet("/addtask")
public class AddTaskServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();

    /**
     * Shows the Add Task form.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        request.getRequestDispatcher("addtask.jsp").forward(request, response);
    }

    /**
     * Processes the form submission to add a new task.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        // Get the logged-in user
        User user = (User) session.getAttribute("user");

        // Get the task title from the form
        String title = request.getParameter("title");

        // Validate input
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "Task title cannot be empty.");
            request.getRequestDispatcher("addtask.jsp").forward(request, response);
            return;
        }

        // Create a new Task object
        Task task = new Task();
        task.setUserId(user.getId());
        task.setTitle(title.trim());

        // Save to database via DAO
        boolean success = taskDAO.addTask(task);

        if (success) {
            // Task added → redirect to dashboard
            response.sendRedirect("dashboard");
        } else {
            request.setAttribute("error", "Failed to add task. Please try again.");
            request.getRequestDispatcher("addtask.jsp").forward(request, response);
        }
    }
}
