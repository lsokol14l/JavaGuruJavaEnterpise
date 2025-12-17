package by.michael;

import by.michael.dao.PathDao;
import by.michael.entity.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PathDaoTests {
  @DisplayName("Проверка наличия полей после сериализации")
  @Test
  public void testMyPathDao_whenInvokeGetPath_withNull_returnsDefaultValue() {
    PathDao pathDao = new PathDao();

    Optional<Path> pathByLifestyle = pathDao.findPathByLifestyle(null);

    assertTrue(pathByLifestyle.isEmpty());
  }
}
