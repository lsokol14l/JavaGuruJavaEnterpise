package by.michael;

import by.michael.dto.JsonDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class HttpServer {
  private final int port;

  public HttpServer(int port) {
    this.port = port;
  }

  public void run() {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      Socket socket = serverSocket.accept();
      processSocket(socket);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void processSocket(Socket socket) {
    // 1) Читаем запрос клиента
    try (InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); ) {

      // 1.1. Читаем заголовочную строку
      String headerRequest = bufferedReader.readLine();
      System.out.println(headerRequest);

      // 1.2. Читаем свойства запроса headers
      List<String> headers = new ArrayList<>();
      String header;
      int contentLength = 0;
      while ((header = bufferedReader.readLine()) != null && !header.isEmpty()) {
        if (header.toLowerCase().startsWith("content-length:"))
          contentLength = Integer.parseInt(header.split(":")[1].trim());
        headers.add(header);
      }

      // 1.3. Читаем body, он может быть не всегда, это стоит помнить (наверное)
      char[] jsonBytes = new char[contentLength];
      int read = bufferedReader.read(jsonBytes, 0, contentLength);
      String body = new String(jsonBytes);
      ObjectMapper objectMapper = new ObjectMapper();
      JsonDto jsonDto = objectMapper.readValue(body, JsonDto.class);

      // 2) Отлично, мы получили данные, теперь нужно ответить клиенту
      // 2.1.

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
