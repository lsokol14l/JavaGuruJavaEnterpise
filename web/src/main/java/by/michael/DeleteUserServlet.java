package by.michael;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete")
public class DeleteUserServlet extends HttpServlet {
  UserService userService = new UserService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String stringId = req.getParameter("id");

    Long id = null;

    try {
      id = Long.parseLong(stringId);
    } catch (Exception e) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    userService.deleteUser(id);
    resp.sendRedirect("/get_users");
  }
}
