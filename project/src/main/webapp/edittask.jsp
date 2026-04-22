<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tasksketch.model.User" %>
<%@ page import="com.tasksketch.model.Task" %>
<%
    // Protect this page
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }

    // Get the task to edit (set by EditTaskServlet)
    Task task = (Task) request.getAttribute("task");
    if (task == null) {
        response.sendRedirect("dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Edit your task on TaskSketch">
    <title>Edit Task — TaskSketch</title>
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

        <!-- ===== Page Header ===== -->
        <h1 class="page-title wavy-underline">Edit Task</h1>
        <p class="page-subtitle">Update the task details below ✎</p>

        <!-- Error Message -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" id="error-alert">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <!-- ===== Edit Task Form ===== -->
        <div class="auth-card" style="max-width: 600px; margin-top: 1.5rem;">
            <form action="edittask" method="POST" id="edit-task-form">
                <!-- Hidden field: task ID -->
                <input type="hidden" name="id" value="<%= task.getId() %>">

                <div class="form-group">
                    <label for="title">Task Title</label>
                    <input type="text" id="title" name="title"
                           value="<%= task.getTitle() %>"
                           required autofocus>
                </div>

                <div style="display: flex; gap: 0.75rem;">
                    <button type="submit" class="btn btn-warning" id="update-task-btn">
                        Update Task ✎
                    </button>
                    <a href="dashboard" class="btn btn-secondary" id="cancel-btn">
                        ← Back
                    </a>
                </div>

            </form>
        </div>

        <!-- ===== Footer ===== -->
        <hr class="sketch-hr">
        <div style="text-align: center; color: var(--text-light); font-size: 0.9rem; padding-bottom: 2rem;">
            TaskSketch — a hand-drawn to-do app &nbsp;✎
        </div>

    </div>

</body>
</html>
