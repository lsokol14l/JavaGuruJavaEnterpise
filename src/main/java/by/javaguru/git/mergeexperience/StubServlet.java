package by.javaguru.git.mergeexperience;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/stub")
public class StubServlet extends HttpServlet {
  private String msg;

  @Override
  public void init() throws ServletException {
    msg = "Материал еще не пройден!";
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();

    writer.println("<html>");
    writer.println("<h3>Упс...</h3>");
    writer.println("<h4>" + msg + "</h4>");
    writer.println("</html>");
  }

  @Override
  public void destroy() {}
}
