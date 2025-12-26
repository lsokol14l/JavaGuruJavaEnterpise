package by.michael.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String username = req.getParameter("name");
    String email = req.getParameter("email");
    String password = req.getParameter("password");
    String re_password = req.getParameter("re_password");
    String phone = req.getParameter("phoneNumber");

    RequestDispatcher dispatcher;

    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    try (Connection connection =
        DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/users?useSSL=false", "postgres", "Cokolsuper12"); ) {

      PreparedStatement pst =
          connection.prepareStatement(
              "insert into \"users\".\"users\" (name, email, phone, password) values (?, ?, ?, ?)");

      pst.setString(1, username);
      pst.setString(2, email);
      pst.setString(3, phone);
      pst.setString(4, password);

      int result = pst.executeUpdate();

      dispatcher = req.getRequestDispatcher("registration.jsp");
      if (result > 0) {
        req.setAttribute("status", "success");
      } else {
        req.setAttribute("status", "failed");
      }

      dispatcher.forward(req, resp);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
