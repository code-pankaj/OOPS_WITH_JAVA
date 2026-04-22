<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Create your TaskSketch account — start managing tasks today">
    <title>Sign Up — TaskSketch</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <!-- Background doodle stars -->
    <span class="doodle-star" style="top:8%; right:10%;">✦</span>
    <span class="doodle-star" style="top:30%; left:6%;">✧</span>
    <span class="doodle-star" style="bottom:15%; right:15%;">✦</span>
    <span class="doodle-star" style="bottom:40%; left:10%;">✧</span>

    <div class="auth-wrapper fade-in">
        <div class="auth-card">

            <!-- Logo / App Name -->
            <div style="text-align: center; margin-bottom: 1.5rem;">
                <span class="brand-doodle"></span>
                <span class="page-title" style="vertical-align: middle;">TaskSketch</span>
                <p class="page-subtitle" style="margin-top: 0.5rem;">
                    Create an account to start sketching your tasks!
                </p>
            </div>

            <!-- Error Message (if any) -->
            <% if (request.getAttribute("error") != null) { %>
                <div class="alert alert-error" id="error-alert">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>

            <!-- Signup Form -->
            <form action="signup" method="POST" id="signup-form">

                <div class="form-group">
                    <label for="name">Full Name</label>
                    <input type="text" id="name" name="name"
                           placeholder="John Doe"
                           value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : "" %>"
                           required>
                </div>

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
                           placeholder="at least 4 characters"
                           required minlength="4">
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword"
                           placeholder="re-enter password"
                           required>
                </div>

                <button type="submit" class="btn btn-success btn-block" id="signup-btn">
                    Create Account ✎
                </button>
            </form>

            <!-- Login Link -->
            <div class="auth-footer">
                Already have an account?
                <a href="login" id="login-link">Log in here</a>
            </div>

        </div>
    </div>

</body>
</html>
