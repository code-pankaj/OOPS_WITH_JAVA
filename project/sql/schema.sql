-- ============================================
-- TaskSketch Database Schema
-- A simple To-Do / Task Manager application
-- ============================================

-- NOTE: Database creation and user setup is handled
-- by the setup script. This file only creates tables
-- and inserts sample data.

-- ============================================
-- USERS TABLE
-- Stores registered user information
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TASKS TABLE
-- Stores tasks belonging to each user
-- ============================================
CREATE TABLE IF NOT EXISTS tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    status ENUM('pending', 'completed') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- ============================================
-- SAMPLE DATA (uses INSERT IGNORE so re-runs are safe)
-- Demo user (password: demo123)
-- ============================================
INSERT IGNORE INTO users (id, name, email, password) VALUES
    (1, 'Demo User', 'demo@example.com', 'demo123');

INSERT IGNORE INTO tasks (id, user_id, title, status) VALUES
    (1, 1, 'Buy groceries from the market', 'pending'),
    (2, 1, 'Complete Java assignment', 'pending'),
    (3, 1, 'Read chapter 5 of DBMS book', 'completed'),
    (4, 1, 'Submit project report', 'pending'),
    (5, 1, 'Call mom', 'completed');
