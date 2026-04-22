<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tasksketch.model.User" %>
<%@ page import="com.tasksketch.model.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Get the logged-in user from session
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    // Get tasks and stats from request attributes (set by DashboardServlet)
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
    int totalCount = request.getAttribute("totalCount") != null ? (int) request.getAttribute("totalCount") : 0;
    int pendingCount = request.getAttribute("pendingCount") != null ? (int) request.getAttribute("pendingCount") : 0;
    int completedCount = request.getAttribute("completedCount") != null ? (int) request.getAttribute("completedCount") : 0;

    // Date formatter for task creation dates
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Your TaskSketch dashboard — manage all your tasks in one place">
    <title>Dashboard — TaskSketch</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="page-wrapper fade-in">

        <!-- ===== Navigation Bar ===== -->
        <nav class="navbar" id="main-nav">
            <a href="dashboard" class="navbar-brand" id="nav-brand">
                <span class="brand-icon">📝</span> TaskSketch
            </a>
            <div class="navbar-right">
                <span class="navbar-user">
                    Hello, <strong><%= user.getName() %></strong>
                </span>
                <a href="logout" class="btn btn-secondary btn-sm" id="logout-btn">Logout</a>
            </div>
        </nav>

        <!-- ===== Dashboard Header ===== -->
        <div class="dashboard-header">
            <div>
                <h1 class="greeting wavy-underline">Your Tasks</h1>
                <p class="greeting-sub">Here's what you need to get done today ✎</p>
            </div>
        </div>

        <!-- ===== Stats Row ===== -->
        <div class="stats-row" id="stats-section">
            <div class="stat-card stat-total">
                <div class="stat-number"><%= totalCount %></div>
                <div class="stat-label">Total</div>
            </div>
            <div class="stat-card stat-pending">
                <div class="stat-number"><%= pendingCount %></div>
                <div class="stat-label">Pending</div>
            </div>
            <div class="stat-card stat-completed">
                <div class="stat-number"><%= completedCount %></div>
                <div class="stat-label">Done</div>
            </div>
        </div>

        <!-- ===== Quick Add Form ===== -->
        <form action="addtask" method="POST" class="quick-add-form" id="quick-add-form">
            <input type="text" name="title" placeholder="✎  Write a new task..." id="quick-add-input" required>
            <button type="submit" class="btn btn-primary" id="quick-add-btn">Add</button>
        </form>

        <!-- ===== Task List Section ===== -->
        <div class="task-header">
            <h2 class="section-title">All Tasks</h2>
            <a href="addtask" class="btn btn-success btn-sm" id="add-task-link">+ New Task</a>
        </div>

        <% if (tasks == null || tasks.isEmpty()) { %>
            <!-- Empty State -->
            <div class="empty-state" id="empty-state">
                <span class="empty-icon">📋</span>
                <p>No tasks yet! Add your first task above.</p>
                <a href="addtask" class="btn btn-primary" id="empty-add-btn">+ Add a Task</a>
            </div>
        <% } else { %>
            <!-- Task Items -->
            <ul class="task-list" id="task-list">
                <% for (Task task : tasks) { %>
                    <li class="task-item <%= task.isCompleted() ? "completed" : "" %>" id="task-<%= task.getId() %>">

                        <!-- Toggle Status Checkbox -->
                        <a href="toggletask?id=<%= task.getId() %>"
                           class="task-checkbox <%= task.isCompleted() ? "checked" : "" %>"
                           title="Toggle status"
                           id="toggle-<%= task.getId() %>">
                            <%= task.isCompleted() ? "✓" : "" %>
                        </a>

                        <!-- Task Body -->
                        <div class="task-body">
                            <div class="task-title"><%= task.getTitle() %></div>
                            <div class="task-date">
                                <span class="badge <%= task.isCompleted() ? "badge-completed" : "badge-pending" %>">
                                    <%= task.getStatus() %>
                                </span>
                                &nbsp;·&nbsp;
                                <%= task.getCreatedAt() != null ? dateFormat.format(task.getCreatedAt()) : "" %>
                            </div>
                        </div>

                        <!-- Action Buttons -->
                        <div class="task-actions">
                            <a href="edittask?id=<%= task.getId() %>"
                               class="task-action-btn edit-btn"
                               title="Edit task"
                               id="edit-<%= task.getId() %>">✎</a>
                            <a href="deletetask?id=<%= task.getId() %>"
                               class="task-action-btn delete-btn"
                               title="Delete task"
                               id="delete-<%= task.getId() %>"
                               onclick="return confirm('Delete this task?');">✕</a>
                        </div>
                    </li>
                <% } %>
            </ul>
        <% } %>

        <!-- ===== Footer ===== -->
        <hr class="sketch-hr">
        <div style="text-align: center; color: var(--text-light); font-size: 0.9rem; padding-bottom: 2rem;">
            TaskSketch — a hand-drawn to-do app &nbsp;✎
        </div>

    </div>

</body>
</html>
