package by.michael;

import by.michael.dto.EmployeeDto;
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
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        OutputStream outputStream = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream))) {

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
      List<EmployeeDto> employees = jsonDto.getEmployees();

      int total_salary = 0;
      int total_tax = 0;
      int total_profit = 0;

      for (EmployeeDto employee : employees) {
        total_salary += employee.getSalary();
        total_tax += employee.getTax();
      }
      total_profit = total_salary - total_tax;

      // 2) Отлично, мы получили данные, теперь нужно ответить клиенту
      // 2.0 сначала нужно составить body, чтобы в заголовке потом указать его длины
      String requestBody =
          """
              <!DOCTYPE html>
              <html lang="en">
              <head>
                 <title>Salary</title>
              </head>
              <body>

              <table>
                 <tr>
                     <th>Total income</th>
                     <th>Total tax</th>
                     <th>Total profit</th>
                 </tr>
                 <tr>
                     <td>%d</td>
                     <td>%d</td>
                     <td>%d</td>
                 </tr>
              </table>
              </body>
              </html>
              """
              .formatted(total_salary, total_tax, total_profit);

      // 2.1. для начала Нужно ответить таким же заголовком ответа
      String headerResponse =
          """
          HTTP/1.1 200 OK
          content-type: text/html
          content-length: %d
          """
              .formatted(requestBody.length());
      bufferedWriter.write(headerResponse);
      bufferedWriter.write(System.lineSeparator());

      bufferedWriter.write(requestBody);
      bufferedWriter.flush();

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
