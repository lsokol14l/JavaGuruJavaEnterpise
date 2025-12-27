package by.michael.registration;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

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

    if (username == null || username.isEmpty()) {
      req.setAttribute("status", "invalidName");
      dispatcher = req.getRequestDispatcher("/registration");
      dispatcher.forward(req, resp);
    }
    if (email == null || email.isEmpty()) {
      req.setAttribute("status", "invalidEmail");
      dispatcher = req.getRequestDispatcher("/registration");
      dispatcher.forward(req, resp);
    }
    if (password == null || password.isEmpty()) {
      req.setAttribute("status", "invalidPassword");
      dispatcher = req.getRequestDispatcher("/registration");
      dispatcher.forward(req, resp);
    }
    if (re_password == null || re_password.isEmpty() || !Objects.equals(password, re_password)) {
      req.setAttribute("status", "invalidRePassword");
      dispatcher = req.getRequestDispatcher("/registration");
      dispatcher.forward(req, resp);
    }
    if (phone == null || phone.isEmpty()) {
      req.setAttribute("status", "invalidPhone");
      dispatcher = req.getRequestDispatcher("/registration");
      dispatcher.forward(req, resp);
    } else if (phone.length() > 10) {
      req.setAttribute("status", "invalidPhoneLength");
      dispatcher = req.getRequestDispatcher("/registration");
      dispatcher.forward(req, resp);
    }

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

      if (result > 0) {
        req.setAttribute("status", "success");
        dispatcher = req.getRequestDispatcher("login.jsp");
        dispatcher.forward(req, resp);
      } else {
        req.setAttribute("status", "failed");
        dispatcher = req.getRequestDispatcher("registration.jsp");
        dispatcher.forward(req, resp);
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
