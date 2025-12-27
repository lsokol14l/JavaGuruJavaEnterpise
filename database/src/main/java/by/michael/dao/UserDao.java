package by.michael.dao;

import by.michael.config.DatabaseConfig;
import by.michael.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
  public void createNewUser(User newUser) {
    String sql = "insert into users.users.users (name, email, phone, password) values (?,?,?,?)";

    try (Connection connection = DatabaseConfig.getConnection();
        PreparedStatement pst = connection.prepareStatement(sql)) {

      pst.setString(1, newUser.getName());
      pst.setString(2, newUser.getLogin());
      pst.setString(3, newUser.getPhoneNumber());
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
}
