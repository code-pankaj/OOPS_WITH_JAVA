package com.tasksketch.dao;

import com.tasksketch.model.Task;
import com.tasksketch.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TaskDAO.java — Data Access Object for Task operations.
 * Handles all CRUD operations on tasks:
 *   - Add a new task
 *   - Get all tasks for a user
 *   - Get a single task by ID
 *   - Update task title
 *   - Toggle task status (pending ↔ completed)
 *   - Delete a task
 */
public class TaskDAO {

    /**
     * Adds a new task to the database.
     *
     * @param task The Task object with userId and title set
     * @return true if insertion was successful
     */
    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks (user_id, title, status) VALUES (?, ?, 'pending')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, task.getUserId());
            pstmt.setString(2, task.getTitle());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all tasks belonging to a specific user.
     * Results are ordered by creation date (newest first).
     *
     * @param userId The ID of the user
     * @return A list of Task objects
     */
    public List<Task> getTasksByUserId(int userId) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setUserId(rs.getInt("user_id"));
                task.setTitle(rs.getString("title"));
                task.setStatus(rs.getString("status"));
                task.setCreatedAt(rs.getTimestamp("created_at"));
                tasks.add(task);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching tasks: " + e.getMessage());
        }

        return tasks;
    }

    /**
     * Gets a single task by its ID.
     *
     * @param taskId The task ID
     * @return The Task object, or null if not found
     */
    public Task getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Task task = new Task();
                task.setId(rs.getInt("id"));
                task.setUserId(rs.getInt("user_id"));
                task.setTitle(rs.getString("title"));
                task.setStatus(rs.getString("status"));
                task.setCreatedAt(rs.getTimestamp("created_at"));
                return task;
            }

        } catch (SQLException e) {
            System.err.println("Error fetching task: " + e.getMessage());
        }

        return null;
    }

    /**
     * Updates the title of an existing task.
     *
     * @param taskId The ID of the task to update
     * @param newTitle The new title
     * @return true if update was successful
     */
    public boolean updateTask(int taskId, String newTitle) {
        String sql = "UPDATE tasks SET title = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newTitle);
            pstmt.setInt(2, taskId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Toggles the status of a task between 'pending' and 'completed'.
     *
     * @param taskId The ID of the task to toggle
     * @return true if toggle was successful
     */
    public boolean toggleTaskStatus(int taskId) {
        // Use a CASE statement to flip the status in one query
        String sql = "UPDATE tasks SET status = CASE WHEN status = 'pending' THEN 'completed' ELSE 'pending' END WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error toggling task status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a task from the database.
     *
     * @param taskId The ID of the task to delete
     * @return true if deletion was successful
     */
    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Counts tasks by status for a specific user.
     *
     * @param userId The user ID
     * @param status The status to count ("pending" or "completed")
     * @return The count of matching tasks
     */
    public int countTasksByStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ? AND status = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Error counting tasks: " + e.getMessage());
        }

        return 0;
    }
}
