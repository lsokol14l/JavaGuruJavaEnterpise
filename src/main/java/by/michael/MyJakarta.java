package by.michael;

import by.michael.entity.Technology;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class MyJakarta {
  private String version;
  private String description;
  private List<Technology> technologies;

  // сделала гпт надо произвести анализ
  private static String defaultPath;

  static {
    Properties properties = new Properties();
    String profile = System.getProperty("env", "dev"); // -Denv=prod / -Denv=test
    try (FileInputStream is = new FileInputStream("config.properties")) {
      properties.load(is);
      defaultPath = properties.getProperty(profile + ".filepath");
      System.out.println("Все ок.");
      System.out.printf(defaultPath);
    } catch (IOException e) {
      defaultPath = "src/main/resources/data.json";
      e.printStackTrace();
      System.out.println("Что-то пошло не так.");
    }
  }

  public MyJakarta() {}

  public MyJakarta(String version, String description, List<Technology> technologies) {
    this.version = version;
    this.description = description;
    this.technologies = technologies;
  }

  public String getVersion() {
    return version;
  }

  public String getDescription() {
    return description;
  }

  public List<Technology> getTechnologies() {
    return technologies;
  }

  public void writeToJson(String path) {
    if (path == null) path = defaultPath;

    try (FileOutputStream outputStream = new FileOutputStream(path)) {

      ObjectMapper objectMapper = new ObjectMapper();

      String result = objectMapper.writeValueAsString(this);

      outputStream.write(result.getBytes());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public MyJakarta readFromJson(String path) {
    if (path == null) path = defaultPath;

    try (FileInputStream inputStream = new FileInputStream(path)) {
      ObjectMapper objectMapper = new ObjectMapper();

      String jsonString = new String(inputStream.readAllBytes());

      return objectMapper.readValue(jsonString, MyJakarta.class);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateTechnology(Technology technology, String path) {
    if (technology == null) return;
    if (path == null) path = defaultPath;

    List<Technology> technologies = getTechnologies();

    boolean updated = false;
    for (int i = 0; i < technologies.size(); i++) {
      if (technologies.get(i).getName().equals(technology.getName())) {
        technologies.set(i, technology);
        updated = true;
      }
    }

    if (!updated) technologies.add(technology);

    writeToJson(path);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    MyJakarta myJakarta = (MyJakarta) o;
    return Objects.equals(version, myJakarta.version)
        && Objects.equals(description, myJakarta.description)
        && Objects.equals(technologies, myJakarta.technologies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, description, technologies);
  }
}
