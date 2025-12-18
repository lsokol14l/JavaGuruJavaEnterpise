package by.michael;

import by.michael.Entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import by.michael.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserDaoTests {
  private String path = "src/main/resources/data.json";
  UserDao userDao = new UserDao();
  ObjectMapper mapper = new ObjectMapper();

  @DisplayName("Проверка создания файла с тестовыми данными")
  @Test
  public void testUserDao_whenCreateInstance_shouldCreateFileWithTestData() throws IOException {
    // 1) Создание экземпляра класса для тестов

    // 2) Выполнение тестируемого метода (Action) в нашем случае выполняется статический блок
    // (автоматически при создании экземпляра)

    // 3) Проверка данных на выходе (Assert)
    File file = new File(path);

    ObjectMapper mapper = new ObjectMapper();
    List<User> users = mapper.readValue(file, new TypeReference<List<User>>() {});

    for (User user : users) {
      assertTrue(user.getName().contains("test"));
      assertNotNull(user.getId());
    }

    assertTrue(file.exists());
  }

  @DisplayName("Проверка метода findUserById с корректным Id")
  @Test
  public void testFindUserById_whenInvokeMethod_withCorrectId_shouldReturnUserWithId()
      throws IOException {
    // 1) Создание экземпляра класса для тестов
    // 2) Выполнение тестируемого метода (Action) в нашем случае выполняется статический блок
    // (автоматически при создании экземпляра)
    Optional<User> userById = userDao.findUserById(3L);

    // 3) Проверка данных на выходе (Assert)
    User user = null;
    if (userById.isPresent()) user = userById.get();

    assertTrue(userById.isPresent());
    assertEquals(3L, user.getId());
    assertEquals("test_3", user.getName());
  }

  @DisplayName("Проверка метода findUserById с несуществующим Id")
  @Test
  public void testFindUserById_whenInvokeMethod_withIncorrectId_shouldReturnOptionalEmpty()
      throws IOException {
    // 1) Создание экземпляра класса для тестов
    // 2) Выполнение тестируемого метода (Action) в нашем случае выполняется статический блок
    // (автоматически при создании экземпляра)
    Optional<User> userById = userDao.findUserById(13L);

    // 3) Проверка данных на выходе (Assert)
    assertTrue(userById.isEmpty());
  }

  @DisplayName("Проверка метода findUserById с null")
  @Test
  public void testFindUserById_whenInvokeMethod_withNullId_shouldReturnOptionalEmpty()
          throws IOException {
    // 1) Создание экземпляра класса для тестов
    // 2) Выполнение тестируемого метода (Action) в нашем случае выполняется статический блок
    // (автоматически при создании экземпляра)
    Optional<User> userById = userDao.findUserById(null);

    // 3) Проверка данных на выходе (Assert)
    assertTrue(userById.isEmpty());
  }
}
