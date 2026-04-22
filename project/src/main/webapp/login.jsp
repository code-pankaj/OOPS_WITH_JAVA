<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Login to TaskSketch — your hand-drawn to-do manager">
    <title>Login — TaskSketch</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <!-- Background doodle stars -->
    <span class="doodle-star" style="top:10%; left:8%;">✦</span>
    <span class="doodle-star" style="top:25%; right:12%;">✧</span>
    <span class="doodle-star" style="bottom:20%; left:15%;">✦</span>
    <span class="doodle-star" style="bottom:35%; right:8%;">✧</span>

    <div class="auth-wrapper fade-in">
        <div class="auth-card">

            <!-- Logo / App Name -->
            <div style="text-align: center; margin-bottom: 1.5rem;">
                <span class="brand-doodle"></span>
                <span class="page-title" style="vertical-align: middle;">TaskSketch</span>
                <p class="page-subtitle" style="margin-top: 0.5rem;">
                    Welcome back! Log in to see your tasks.
                </p>
            </div>

            <!-- Error Message (if any) -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error" id="error-alert">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <!-- Success Message (e.g., after signup) -->
            <% if (request.getAttribute("success") != null) { %>
                <div class="alert alert-success" id="success-alert">
                    <%= request.getAttribute("success") %>
                </div>
            <% } %>

            <!-- Login Form -->
            <form action="login" method="POST" id="login-form">

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email"
                           placeholder="you@example.com"
                           value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                           required>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password"
                           placeholder="your password"
                           required>
                </div>

                <button type="submit" class="btn btn-primary btn-block" id="login-btn">
                    Log In →
                </button>
            </form>

            <!-- Signup Link -->
            <div class="auth-footer">
                Don't have an account?
                <a href="signup" id="signup-link">Sign up here</a>
            </div>

        </div>
    </div>

</body>
</html>
