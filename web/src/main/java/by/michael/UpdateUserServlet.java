package by.michael;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/update")
public class UpdateUserServlet extends HttpServlet {
  UserService userService = new UserService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String stingId = req.getParameter("id");
    String name = req.getParameter("name");

    Long id = null;
    try {
      id = Long.parseLong(stingId);
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    userService.updateUser(new UserDto(id, name));

    resp.sendRedirect("/get_users");
  }
}
