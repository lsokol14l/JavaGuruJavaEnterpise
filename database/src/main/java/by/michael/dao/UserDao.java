package by.michael.dao;

import by.michael.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
  public static final String FILEPATH;
  ObjectMapper objectMapper = new ObjectMapper();
  List<User> users = new ArrayList<>();

  static {
    FILEPATH = System.getenv("CATALINA_BASE") + "/data.json";
    File file = new File(FILEPATH);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        System.err.println("check filepath for data.json: " + FILEPATH);
        throw new RuntimeException(e);
      }
    }
  }

  public void createNewUser(User newUser) {
    users.add(newUser);
    writeToJson();
  }

  private void updateDataFromJson() {
    File file = new File(FILEPATH);
    try {
      users = objectMapper.readValue(file, new TypeReference<>() {});
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void writeToJson() {
    File file = new File(FILEPATH);
    try {
      objectMapper.writeValue(file, users);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
