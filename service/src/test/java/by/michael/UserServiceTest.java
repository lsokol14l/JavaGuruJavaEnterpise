package by.michael;

import by.michael.dto.UserDto;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
  private static UserService userService;

  @BeforeAll
  static void setUp() {
    userService = new UserService();
  }

  @Test
  @Order(1)
  void testCreateNewUser() {
    UserDto newUser =
        new UserDto("Service Test", "servicetest@example.com", "+9876543210", "password123");

    assertDoesNotThrow(() -> userService.createNewUser(newUser));
  }

  @Test
  @Order(2)
  void testAuthenticate_Success() {
    UserDto authenticated = userService.authenticate("servicetest@example.com", "password123");

    assertNotNull(authenticated);
    assertEquals("Service Test", authenticated.getName());
    assertEquals("servicetest@example.com", authenticated.getLogin());
  }

  @Test
  @Order(3)
  void testAuthenticate_WrongPassword() {
    UserDto authenticated = userService.authenticate("servicetest@example.com", "wrongpass");

    assertNull(authenticated);
  }

  @Test
  @Order(4)
  void testAuthenticate_UserNotFound() {
    UserDto authenticated = userService.authenticate("nobody@example.com", "password123");

    assertNull(authenticated);
  }
}
