<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error — TaskSketch</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="auth-wrapper fade-in">
        <div class="auth-card" style="text-align: center;">

            <div style="font-size: 4rem; margin-bottom: 1rem;">😕</div>

            <h1 class="page-title">Oops!</h1>
            <p class="page-subtitle" style="margin-bottom: 1.5rem;">
                Something went wrong. Don't worry, it happens to the best of us.
            </p>

            <a href="dashboard" class="btn btn-primary" id="back-home-btn">
                ← Back to Dashboard
            </a>

            <div class="auth-footer" style="margin-top: 1.5rem;">
                Or <a href="login" id="login-link">log in again</a>
            </div>

        </div>
    </div>
</body>
</html>
