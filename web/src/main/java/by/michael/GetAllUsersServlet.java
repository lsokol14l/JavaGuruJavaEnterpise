package by.michael;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/get_users")
public class GetAllUsersServlet extends HttpServlet {
  UserService userService = new UserService();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/html");

    List<UserDto> users = userService.getAllUsers();

    PrintWriter writer = resp.getWriter();

    writer.println("<html><body>");
    for (UserDto user : users) {
      writer.println("<h2>Id = " + user.getId() + " name: " + user.getName() + "</h2>");
    }
    writer.println("</html></body>");

    writer.close();
  }
}
