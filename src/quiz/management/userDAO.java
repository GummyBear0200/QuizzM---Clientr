
package quiz.management;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class userDAO {
    private Connection connection;

    public userDAO(Connection connection) {
        this.connection = connection;
    }

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (user_id, username, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId()); 
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        }
    }


    public List<User> viewUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                users.add(user);
            }
        }
        return users;
    }

    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET username = ?, email = ? WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setInt(3, user.getUserId());
            stmt.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}