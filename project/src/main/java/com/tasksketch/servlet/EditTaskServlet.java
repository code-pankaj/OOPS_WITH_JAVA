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
 * EditTaskServlet.java — Handles editing an existing task.
 *
 * GET  /edittask?id=X  → Show the edit form pre-filled with task data
 * POST /edittask       → Process the form and update the task
 */
@WebServlet("/edittask")
public class EditTaskServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();

    /**
     * Shows the edit form with current task data.
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

        // Get the task ID from the query parameter
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("dashboard");
            return;
        }

        try {
            int taskId = Integer.parseInt(idStr);
            Task task = taskDAO.getTaskById(taskId);

            // Make sure the task belongs to the logged-in user
            User user = (User) session.getAttribute("user");
            if (task == null || task.getUserId() != user.getId()) {
                response.sendRedirect("dashboard");
                return;
            }

            request.setAttribute("task", task);
            request.getRequestDispatcher("edittask.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard");
        }
    }

    /**
     * Processes the edit form submission.
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

        User user = (User) session.getAttribute("user");

        // Get form parameters
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");

        // Validate input
        if (idStr == null || title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "Task title cannot be empty.");
            request.getRequestDispatcher("edittask.jsp").forward(request, response);
            return;
        }

        try {
            int taskId = Integer.parseInt(idStr);

            // Verify task ownership
            Task task = taskDAO.getTaskById(taskId);
            if (task == null || task.getUserId() != user.getId()) {
                response.sendRedirect("dashboard");
                return;
            }

            // Update the task
            taskDAO.updateTask(taskId, title.trim());
            response.sendRedirect("dashboard");

        } catch (NumberFormatException e) {
            response.sendRedirect("dashboard");
        }
    }
}
