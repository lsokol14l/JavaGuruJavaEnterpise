package by.michael;

import by.michael.entity.Technology;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class MyJakarta {
  private String version;
  private String description;
  private List<Technology> technologies;
  private String path;

  public MyJakarta() {}

  public MyJakarta(String version, String description, List<Technology> technologies) {
    this.version = version;
    this.description = description;
    this.technologies = technologies;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTechnologies(List<Technology> technologies) {
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
    Optional<Technology> first =
        technologies.stream()
            .filter(tech -> tech.getName().equals(technology.getName()))
            .findFirst();
    if (first.isPresent()) {
      Technology oldTechnology = first.get();
      oldTechnology = technology;
    } else {
      technologies.add(technology);
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
