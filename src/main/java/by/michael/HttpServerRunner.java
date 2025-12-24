package by.michael;

public class HttpServerRunner {
  static void main() {
    HttpServer httpServer = new HttpServer(8082);
    httpServer.run();
  }
}
