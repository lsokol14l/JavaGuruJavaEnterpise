package by.michael.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static by.michael.utils.ConnectionManager.DB_URL_KEY;
import static by.michael.utils.ConnectionManager.DB_USER_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConnectionManagerTests {
  @DisplayName("Testing get connection")
  @Test
  void testGetConnection() {
    Connection connection = ConnectionManager.get();
    assertNotNull(connection);
    try {
      assertEquals(PropertiesUtil.getProperty(DB_URL_KEY), connection.getMetaData().getURL());
      assertEquals(PropertiesUtil.getProperty(DB_USER_KEY), connection.getMetaData().getUserName());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
