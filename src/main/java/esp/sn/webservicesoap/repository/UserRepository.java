package esp.sn.webservicesoap.repository;

import esp.sn.webservicesoap.model.User;
import esp.sn.webservicesoap.util.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try(Connection conn = Database.connection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                User user = new User(
                    resultSet.getLong("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("role")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }
    public Long addUser(String username, String password, String role) {
        long id = 0;
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try(Connection conn = Database.connection();
            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void updateUser(String username, String password, String role) {
        String query = "UPDATE users SET password = ?, role = ? WHERE username = ?";
        try(
                Connection conn = Database.connection();
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                ) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, role);
            preparedStatement.setString(3, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        try(
                Connection conn = Database.connection();
                PreparedStatement preparedStatement = conn.prepareStatement(query)
                ) {
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
