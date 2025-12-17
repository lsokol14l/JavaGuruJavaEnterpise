package by.michael;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/path")
public class PathServlet extends HttpServlet {
  private final PathService pathService = new PathService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");

    Optional<PathDto> discipline = pathService.getPath("discipline");
    Optional<PathDto> hardworking = pathService.getPath("hardworking");
    Optional<PathDto> anesthesia = pathService.getPath("anesthesia");

    PrintWriter writer = resp.getWriter();

    writer.write("<html><body>");
    writer.write("<h2>" + discipline.get().getPath() + "</h2>");
    writer.write("<img src=\"" + discipline.get().getImageUrl() + "\" alt=\"discipline path\">");
    writer.write("<h2>" + hardworking.get().getPath() + "</h2>");
    writer.write("<img src=\"" + hardworking.get().getImageUrl() + "\" alt=\"hardworking path\">");
    writer.write("<h2>" + anesthesia.get().getPath() + "</h2>");
    writer.write("<img src=\"" + anesthesia.get().getImageUrl() + "\" alt=\"anesthesia path\">");
    writer.write("<br>");
    writer.write(System.getProperty("user.dir"));
    writer.write("</html></body>");

    writer.close();
  }
}
