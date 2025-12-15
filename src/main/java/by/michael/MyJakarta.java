package by.michael;

import by.michael.entity.Technology;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MyJakarta {
  private String version;
  private String description;
  private List<Technology> technologies;

  private String path;

  public void writeToJson(String path) {

    try (FileOutputStream outputStream = new FileOutputStream(path); ) {
      ObjectMapper objectMapper = new ObjectMapper();

      String result = objectMapper.writeValueAsString(this);

      outputStream.write(result.getBytes());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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

  public MyJakarta readFromJson(String path) {
    try (FileInputStream inputStream = new FileInputStream(path)) {
      ObjectMapper mapper = new ObjectMapper();

      String jsonString = Arrays.toString(inputStream.readAllBytes());

      JsonNode jsonNode = mapper.readTree(jsonString);

      String version = String.valueOf(jsonNode.get("version"));
      String description = String.valueOf(jsonNode.get("description"));
      JsonNode technologies = jsonNode.get("technologies");
      List<Technology> technologyList = new ArrayList<>();

      for (JsonNode technology : technologies) {
        technologyList.add(
            new Technology(
                String.valueOf(technology.get("name")),
                String.valueOf(technology.get("description"))));
      }

      return new MyJakarta(version, description, technologyList);
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
}
