package by.michael.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
  private static final String URL_KEY = "db.url";
  private static final String USERNAME_KEY = "db.username";
  private static final String PASSWORD_KEY = "db.password";
  private static final String DRIVER_KEY = "db.driver";

  static {
    try {
      Class.forName(PropertiesLoader.getProperty(DRIVER_KEY));
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("PostgreSQL Driver not found", e);
    }
  }

  public static Connection getConnection() throws SQLException {

    return DriverManager.getConnection(
        PropertiesLoader.getProperty(URL_KEY),
        PropertiesLoader.getProperty(USERNAME_KEY),
        PropertiesLoader.getProperty(PASSWORD_KEY));
  }
}
