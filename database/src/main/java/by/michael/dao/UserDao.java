package by.michael.dao;

import by.michael.Entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDao {
  public static String FILEPATH = System.getenv("CATALINA_BASE") + "\\data" + "\\data.json";
  public static ObjectMapper mapper = new ObjectMapper();
  private List<User> users;

  static {
    int index = 10;
    String tomcatPath = System.getenv("CATALINA_BASE") + "\\data";
    File dir = new File(tomcatPath);

    if (!dir.exists()) dir.mkdir();

    File file = new File(FILEPATH);

    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      ObjectMapper mapper = new ObjectMapper();
      List<User> users = new ArrayList<>(index);
      for (int i = 0; i < index; i++) {
        User user = new User((long) i, "test_" + i);
        users.add(user);
      }

      try {
        mapper.writeValue(file, users);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void loadDataFromJson() throws IOException {
    File file = new File(FILEPATH);
    users = mapper.readValue(file, new TypeReference<List<User>>() {});
  }

  public void updateJson() throws IOException {
    File file = new File(FILEPATH);
    mapper.writeValue(file, users);
  }

  public Optional<User> findUserById(Long id) {
    if (id == null) return Optional.empty();

    try {
      loadDataFromJson();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return users.stream().filter(user -> user.getId().equals(id)).findFirst();
  }

  public List<User> findAllUsers() {
    try {
      loadDataFromJson();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return users;
  }

  public void updateUser(User newUser) {
    if (newUser == null) return;

    Optional<User> currentUser = findUserById(newUser.getId());
    if (currentUser.isPresent()) currentUser.get().setName(newUser.getName());
    else {
      users.add(newUser);
    }

    try {
      updateJson();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteUser(Long id) {
    if (id == null) return;

    try {
      loadDataFromJson();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Optional<User> userToDelete =
        users.stream().filter(user -> user.getId().equals(id)).findFirst();

    userToDelete.ifPresent(user -> users.remove(user));

    try {
      updateJson();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
