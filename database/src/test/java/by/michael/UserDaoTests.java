package by.michael;

import by.michael.dao.UserDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static by.michael.dao.UserDao.FILEPATH;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDaoTests {
  @DisplayName("Проверка создания файла data.json")
  @Test
  public void testUserDao_whenCreateInstance_shouldCreateFile() throws IOException {
    // 1) Создание экземпляра класса для тестов

    // 2) Выполнение тестируемого метода (Action) в нашем случае выполняется статический блок
    // (автоматически при создании экземпляра)

    // 3) Проверка данных на выходе (Assert)
    UserDao userDao = new UserDao();
    File file = new File(FILEPATH);

    assertTrue(file.exists());
  }
}
