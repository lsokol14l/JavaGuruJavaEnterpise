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
      String userLink =
          """
            <h2><a href="/user?id=%d">Id = %d, name = %s</a></h2>
          """
              .formatted(user.getId(), user.getId(), user.getName());
      writer.println(userLink);
    }
    writer.println(
"""
<form action="update" method="post">
  <div>
    <label for="name">Введите id пользователя: </label>
    <input type="text" name="id" id="id" required />
  </div>
  <div>
    <label for="email">Введите новое имя: </label>
    <input type="name" name="name" id="name" required />
  </div>
  <div>
    <input type="submit" value="изменить" />
  </div>
</form>
""");
    writer.println("</html></body>");

    writer.close();
  }
}
