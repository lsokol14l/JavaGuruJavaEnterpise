package by.michael;

import by.michael.entity.Technology;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyJakartaTest {

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

  @DisplayName("Проверка метода writeToJson()")
  @Test
  public void testWriteToJson_whenInvoke_thenCreateAJsonFileWithData() {
    List<Technology> technologyList = new ArrayList<Technology>();
    technologyList.add(new Technology("maven", "instrument for automatic building projects"));
    technologyList.add(
        new Technology("jackson", "instrument for serialize/deserialize json objects"));
    technologyList.add(new Technology("junit", "The programmer-friendly testing framework for Java and the JVM"));

    MyJakarta myJakarta =
        new MyJakarta("1.0-SNAPSHOT", "my first Jakarta EE technologies", technologyList);

    myJakarta.writeToJson("src/main/resources/data.json");
  }
}
