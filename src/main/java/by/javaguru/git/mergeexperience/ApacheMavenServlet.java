package by.javaguru.git.mergeexperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/maven")
public class ApacheMavenServlet extends HttpServlet {
  private String content;

  @Override
  public void init() throws ServletException {
    content =
"""
Maven - автоматизированная система сборки, которая позволяет удобно собирать проекты Java,\s
управлять их жизненным циклом, контролировать версии проекта, управлять зависимостями и плагинами.
""";
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();

    writer.println("<html>" + content + "</html>");
  }

  @Override
  public void destroy() {}
}
