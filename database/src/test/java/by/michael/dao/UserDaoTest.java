package by.michael.dao;

import by.michael.entity.User;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDaoTest {
  private static UserDao userDao;

  @BeforeAll
  static void setUp() {
    userDao = new UserDao();
  }

  @Test
  @Order(1)
  void testCreateNewUser() {
    User testUser = new User("Test User", "test@example.com", "+1234567890", "testpass");

    assertDoesNotThrow(() -> userDao.createNewUser(testUser));
  }

  @Test
  @Order(2)
  void testFindByEmailAndPassword_Success() {
    User found = userDao.findByEmailAndPassword("test@example.com", "testpass");

    assertNotNull(found);
    assertEquals("Test User", found.getName());
    assertEquals("test@example.com", found.getLogin());
  }

  @Test
  @Order(3)
  void testFindByEmailAndPassword_WrongPassword() {
    User found = userDao.findByEmailAndPassword("test@example.com", "wrongpass");

    assertNull(found);
  }

  @Test
  @Order(4)
  void testFindByEmailAndPassword_UserNotFound() {
    User found = userDao.findByEmailAndPassword("nonexistent@example.com", "testpass");

    assertNull(found);
  }
}
