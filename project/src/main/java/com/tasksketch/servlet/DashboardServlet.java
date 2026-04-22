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
import java.util.List;

/**
 * DashboardServlet.java — Displays the user's task dashboard.
 *
 * GET /dashboard → Fetch all tasks for the logged-in user and show dashboard.jsp
 *
 * This is the main page after login. It shows:
 *   - Greeting with user's name
 *   - Task statistics (pending, completed, total)
 *   - List of all tasks with action buttons
 */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

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

        // Get the logged-in user from session
        User user = (User) session.getAttribute("user");

        // Fetch all tasks for this user from the database
        List<Task> tasks = taskDAO.getTasksByUserId(user.getId());

        // Get task counts for the stats section
        int pendingCount = taskDAO.countTasksByStatus(user.getId(), "pending");
        int completedCount = taskDAO.countTasksByStatus(user.getId(), "completed");
        int totalCount = tasks.size();

        // Set attributes for the JSP view
        request.setAttribute("tasks", tasks);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("completedCount", completedCount);
        request.setAttribute("totalCount", totalCount);

        // Forward to the dashboard view
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
