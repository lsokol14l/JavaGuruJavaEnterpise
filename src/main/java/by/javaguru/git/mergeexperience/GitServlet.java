package by.javaguru.git.mergeexperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/git")
public class GitServlet extends HttpServlet {
  private String content;

  @Override
  public void init() throws ServletException {
    content =
        """
                Git - это система контроля версий с распределенной архитектурой.
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
