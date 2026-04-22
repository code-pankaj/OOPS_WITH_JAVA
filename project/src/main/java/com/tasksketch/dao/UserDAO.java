package com.tasksketch.dao;

import com.tasksketch.model.User;
import com.tasksketch.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserDAO.java — Data Access Object for User operations.
 * Handles all database interactions related to users:
 *   - Register (insert) a new user
 *   - Authenticate (login) a user
 *   - Check if an email already exists
 */
public class UserDAO {

    /**
     * Registers a new user in the database.
     *
     * @param user The User object with name, email, and password
     * @return true if registration was successful, false otherwise
     */
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user by email and password.
     *
     * @param email    The user's email
     * @param password The user's password
     * @return The User object if credentials match, null otherwise
     */
    public User loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Build and return a User object from the result set
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                return user;
            }

        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
        }

        return null; // Login failed
    }

    /**
     * Checks if an email is already registered.
     *
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    public boolean isEmailTaken(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            System.err.println("Error checking email: " + e.getMessage());
            return false;
        }
    }
}
