package by.michael;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
  UserService userService = new UserService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");

    String stringId = req.getParameter("id");

    if (stringId == null || stringId.isEmpty()) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Long id = null;

    try {
      id = Long.parseLong(stringId);
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    Optional<UserDto> user = userService.getUser(id);

    var writer = resp.getWriter();
    writer.println("<html><body>");
    if (user.isPresent()) {
      writer.println(
          "<h2>User_id: " + user.get().getId() + ", UserName: " + user.get().getName() + "</h2>");
    } else {
      writer.println("пользователь с id: " + id + " не найден!");
    }
    writer.println("</html></body>");
    writer.close();
  }
}
