package by.michael;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

import static java.net.http.HttpRequest.BodyPublishers.ofFile;

public class HttpClient {
  static void main() throws FileNotFoundException {
    var client =
        java.net.http.HttpClient.newBuilder()
            .version(java.net.http.HttpClient.Version.HTTP_1_1)
            .build();

    var requst =
        HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8082"))
            .version(java.net.http.HttpClient.Version.HTTP_1_1)
            .POST(ofFile(Path.of("src/main/resources/request.json")))
            .build();

    HttpResponse<String> response;
    try (client) {
      response = client.send(requst, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

    System.out.println(response.headers());
    System.out.println(response.body());

    createFile(response.body());
  }

  private static void createFile(String body) {
    try (FileWriter fileWriter = new FileWriter("src/main/resources/body.html", false)) {
      fileWriter.write(body);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
