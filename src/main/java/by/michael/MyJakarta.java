package by.michael;

import by.michael.entity.Technology;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class MyJakarta {
  private String version;
  private String description;
  private List<Technology> technologies;

  private String path;

  {
    Properties properties = new Properties();
    try (FileInputStream fileInputStream = new FileInputStream("config.properties")) {
      properties.load(fileInputStream);
      path = properties.getProperty()
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    readFromJson(path);
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
    if (path == null) return;

    try (FileOutputStream outputStream = new FileOutputStream(path)) {

      ObjectMapper objectMapper = new ObjectMapper();

      String result = objectMapper.writeValueAsString(this);

      outputStream.write(result.getBytes());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public MyJakarta readFromJson(String path) {
    try (FileInputStream inputStream = new FileInputStream(path)) {
      ObjectMapper objectMapper = new ObjectMapper();

      String jsonString = new String(inputStream.readAllBytes());

      return objectMapper.readValue(jsonString, MyJakarta.class);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void updateTechnology(Technology technology, String path) {
    if (technology == null || path == null) return;

    List<Technology> technologies = getTechnologies();

    for (int i = 0; i < technologies.size(); i++) {
      if (technologies.get(i).getName().equals(technology.getName()))
        technologies.set(i, technology);
    }

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
