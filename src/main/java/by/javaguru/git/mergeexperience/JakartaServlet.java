package by.javaguru.git.mergeexperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/jakarta")
public class JakartaServlet extends HttpServlet {
  private String content;

  @Override
  public void init() throws ServletException {
    content =
        """
                JakartaEE - стандарт который является продолжением стандарта JavaEE,\s
                на котором основана вся Enterprise разработка (сложных корпоративных систем) \s
                технологии позволяют легковесно, быстро и удобно создавать сложные корпоративные системы.
               \s""";
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
