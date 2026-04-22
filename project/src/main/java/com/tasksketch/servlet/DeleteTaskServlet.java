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
 * DeleteTaskServlet.java — Deletes a task.
 *
 * GET /deletetask?id=X → Delete the task and redirect back to dashboard.
 */
@WebServlet("/deletetask")
public class DeleteTaskServlet extends HttpServlet {

    private TaskDAO taskDAO = new TaskDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is logged in
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        String idStr = request.getParameter("id");

        if (idStr != null) {
            try {
                int taskId = Integer.parseInt(idStr);

                // Verify that the task belongs to the logged-in user
                Task task = taskDAO.getTaskById(taskId);
                if (task != null && task.getUserId() == user.getId()) {
                    taskDAO.deleteTask(taskId);
                }

            } catch (NumberFormatException e) {
                // Invalid ID, ignore
            }
        }

        // Redirect back to dashboard
        response.sendRedirect("dashboard");
    }
}
