package by.michael.dao;

import by.michael.config.DatabaseConfig;
import by.michael.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
  public void createNewUser(User newUser) {
    String sql = "insert into users.users.users (name, email, phone, password) values (?,?,?,?)";

    try (Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql)) {

      pst.setString(1, newUser.getName());
      pst.setString(2, newUser.getEmail());
      pst.setString(3, newUser.getPhone());
      pst.setString(4, newUser.getPassword());

      pst.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error creating user", e);
    }
  }

  public User findByEmailAndPassword(String email, String password) {
    String sql = "select * from users.users.users u where u.email = ? and u.password = ?";

    try (Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql); ) {

      pst.setString(1, email);
      pst.setString(2, password);

      ResultSet resultSet = pst.executeQuery();

      if (resultSet.next()) {
        return new User(
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("phone"),
            resultSet.getString("password"));
      }

    } catch (SQLException e) {
      throw new RuntimeException("Error finding user", e);
    }

    return null;
  }

  public User findByEmail(String email) {
    String sql = "select * from users.users.users u where u.email = ?;";

    try (Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql); ) {
      pst.setString(1, email);

      ResultSet resultSet = pst.executeQuery();

      if (resultSet.next()) {
        return new User(
            resultSet.getString("name"),
            resultSet.getString("email"),
            resultSet.getString("phone"),
            resultSet.getString("password"));
      }

      return null;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
