<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.tasksketch.model.User" %>
<%
    // Protect this page - redirect to login if not authenticated
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Add a new task to your TaskSketch dashboard">
    <title>Add Task — TaskSketch</title>
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
        <h1 class="page-title wavy-underline">Add a New Task</h1>
        <p class="page-subtitle">What do you need to get done? ✎</p>

        <!-- Error Message -->
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-error" id="error-alert">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <!-- ===== Add Task Form ===== -->
        <div class="auth-card" style="max-width: 600px; margin-top: 1.5rem;">
            <form action="addtask" method="POST" id="add-task-form">

                <div class="form-group">
                    <label for="title">Task Title</label>
                    <input type="text" id="title" name="title"
                           placeholder="e.g. Buy groceries, Finish homework..."
                           required autofocus>
                </div>

                <div style="display: flex; gap: 0.75rem;">
                    <button type="submit" class="btn btn-success" id="submit-task-btn">
                        Add Task ✓
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
