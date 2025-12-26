package by.michael.login;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 1. Парсим данные с запроса
    String name = req.getParameter("username");
    String password = req.getParameter("password");

    // 3. Для передачи данных между страничками нужно создать Session
    HttpSession session = req.getSession();

    // 0. Непонятный момент почему без этого кода не работает
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    // 2. Нужно проверить существует ли пользователь?
    try (Connection connection =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/users", "postgres", "Cokolsuper12"); ) {
      PreparedStatement pst =
          connection.prepareStatement(
              "select * from \"users\".\"users\" where email=? and password=?;");
      pst.setString(1, name);
      pst.setString(2, password);
      RequestDispatcher requestDispatcher;
      ResultSet resultSet = pst.executeQuery();

      if (resultSet.next()) {
        session.setAttribute("name", resultSet.getString("name"));
        requestDispatcher = req.getRequestDispatcher("index.jsp");
      } else {
        req.setAttribute("status", "failed");
        requestDispatcher = req.getRequestDispatcher("login.jsp");
      }

      requestDispatcher.forward(req, resp);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
