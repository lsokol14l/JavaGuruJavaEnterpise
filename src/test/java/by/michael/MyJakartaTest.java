package by.michael;

import by.michael.entity.Technology;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyJakartaTest {

  private String path = "src/main/resources/data.json";

  @DisplayName("Проверка наличия полей после сериализации")
  @Test
  public void testMyJakarta_whenInvokeSerialize_returnsSerializedObject_containsMyJakartaFields()
      throws JsonProcessingException {
    List<Technology> technologyList = new ArrayList<Technology>();
    technologyList.add(new Technology("maven", "instrument for automatic building projects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);

    ObjectMapper objectMapper = new ObjectMapper();

    String serializedMyJakartaJson = objectMapper.writeValueAsString(myJakarta);

    JsonNode jsonNode = objectMapper.readTree(serializedMyJakartaJson);
    JsonNode technologies = jsonNode.get("technologies");

    assertTrue(jsonNode.has("version"));
    assertTrue(jsonNode.has("description"));
    assertTrue(jsonNode.has("technologies"));
    assertEquals(2, jsonNode.get("technologies").size());
    assertTrue(jsonNode.get("technologies").isArray());
    assertTrue(technologies.get(1).has("name"));
    assertTrue(technologies.get(1).has("description"));
  }

  @DisplayName("Проверка метода writeToJson() и readFromJson() с корректными данными")
  @Test
  public void testWriteToJson_whenInvoke_thenCreateAJsonFileWithData() {
    List<Technology> technologyList = new ArrayList<Technology>();
    technologyList.add(new Technology("maven", "instrument for automatic building projects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(
        new Technology("junit", "The programmer-friendly testing framework for Java and the JVM"));

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);

    myJakarta.writeToJson("src/main/resources/data.json");

    MyJakarta result = new MyJakarta().readFromJson("src/main/resources/data.json");

    assertEquals(myJakarta, result);
  }

  @DisplayName("Проверка метода writeToJson() и readFromJson() с пустым списком технологий")
  @Test
  public void testWriteToJson_whenInvoke_thenCreateAJsonFileWithEmptyTechnologyList() {
    List<Technology> technologyList = new ArrayList<Technology>();

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);

    myJakarta.writeToJson("src/main/resources/data.json");

    MyJakarta result = new MyJakarta().readFromJson("src/main/resources/data.json");

    assertEquals(myJakarta, result);
  }

  @DisplayName("Проверка метода writeToJson() и readFromJson() с пустым списком технологий")
  @Test
  public void testWriteToJson_whenInvoke_thenCreateAJsonFileWithEmptyData() {
    MyJakarta myJakarta = new MyJakarta(null, null, null);

    myJakarta.writeToJson("src/main/resources/data.json");

    MyJakarta result = new MyJakarta().readFromJson("src/main/resources/data.json");

    assertEquals(myJakarta, result);
  }

  @DisplayName("Проверка метода writeToJson() и readFromJson() с неправильным путем")
  @Test
  public void testWriteToJson_withNullPath_whenInvoke_thenCreateAJsonFileWithEmptyData() {
    MyJakarta myJakarta = new MyJakarta(null, null, null);

    myJakarta.writeToJson(null);

    assertTrue(true);
  }

  @DisplayName("Проверка метода updateTechnology() с корректными данными без повторения")
  @Test
  public void
      testUpdateTechnology_whenInvokeUpdateTechnology_shouldUpdateData_withSimpleDataWithoutRepetitions() {
    List<Technology> technologyList = new ArrayList<>();
    technologyList.add(new Technology("maven", "instrument for automatic building projects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(
        new Technology("junit", "The programmer-friendly testing framework for Java and the JVM"));

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);
    MyJakarta myOldJakarta =
        new MyJakarta(
            "1.0-SNAPSHOT", "my first Jakarta EE technologies", new ArrayList<>(technologyList));

    myJakarta.writeToJson(path);

    Technology updatedJackson =
        new Technology("jackson", "simple instrument for serialize/deserialize json objects");
    myJakarta.updateTechnology(updatedJackson, path);

    MyJakarta result = new MyJakarta().readFromJson(path);

    assertEquals(myJakarta, result);
    assertNotEquals(myOldJakarta, result);
  }

  @DisplayName("Проверка метода updateTechnology() с корректными данными с повторениями")
  @Test
  public void
      testUpdateTechnology_whenInvokeUpdateTechnology_shouldUpdateData_withSimpleDataWithRepetitions() {
    List<Technology> technologyList = new ArrayList<>();
    technologyList.add(new Technology("maven", "instrument for automatic building projects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(
        new Technology("junit", "The programmer-friendly testing framework for Java and the JVM"));

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);
    MyJakarta myOldJakarta =
        new MyJakarta(
            "1.0-SNAPSHOT", "my first Jakarta EE technologies", new ArrayList<>(technologyList));

    myJakarta.writeToJson(path);

    Technology updatedJackson =
        new Technology("jackson", "simple instrument for serialize/deserialize json objects");
    myJakarta.updateTechnology(updatedJackson, path);

    MyJakarta result = new MyJakarta().readFromJson(path);

    List<Technology> technologies = result.getTechnologies();

    for (Technology technology : technologies) {
      if (technology.getName().equals("jackson")) {
        assertEquals(
            "simple instrument for serialize/deserialize json objects",
            technology.getDescription());
      }
    }

    assertEquals(myJakarta, result);
    assertNotEquals(myOldJakarta, result);
  }

  @DisplayName("Проверка метода updateTechnology() с вставкой null")
  @Test
  public void testUpdateTechnology_withNullData_whenInvokeUpdateTechnology_shouldNotUpdateData() {
    List<Technology> technologyList = new ArrayList<Technology>();
    technologyList.add(new Technology("maven", "instrument for automatic building projects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(
        new Technology("junit", "The programmer-friendly testing framework for Java and the JVM"));

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);
    MyJakarta myOldJakarta =
        new MyJakarta(
            "1.0-SNAPSHOT", "my first Jakarta EE technologies", new ArrayList<>(technologyList));

    myJakarta.writeToJson(path);

    myJakarta.updateTechnology(null, path);

    MyJakarta result = new MyJakarta().readFromJson(path);

    assertEquals(myJakarta, result);
    assertEquals(myOldJakarta, result);
  }
}
