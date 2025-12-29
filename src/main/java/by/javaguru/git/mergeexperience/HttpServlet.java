package by.javaguru.git.mergeexperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/servlet")
public class HttpServlet extends jakarta.servlet.http.HttpServlet {
  private String content;

  @Override
  public void init() throws ServletException {
    content =
"""
Как и было сказано ранее Servlet - это Java class, работающий по принципу Singleton, который позволяет работать с запросами/ответами,
а HttpServlet - его конкретная реализация под протокол HTTP.
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
