package by.javaguru.git.mergeexperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/tomcat")
public class ApacheTomcatServlet extends HttpServlet {
  private String content;

  @Override
  public void init() throws ServletException {
    content =
"""
Tomcat - веб библиотека которая позволяет принимать/отправлять http запросы/ответы (coyote),\s
писать свои Servlet в которых обрабатываются запросы/формируются ответы (catalina), взаимодействовать с jsp(jasper).
""";
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();

    writer.println("<html>" + content + "</html>");
  }
}
