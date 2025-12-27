package by.michael.registration;

import by.michael.UserService;
import by.michael.dto.UserDto;
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
  UserService userService = new UserService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String username = req.getParameter("name");
    String email = req.getParameter("email");
    String password = req.getParameter("password");
    String re_password = req.getParameter("re_password");
    String phone = req.getParameter("phoneNumber");

    if (username == null || username.isEmpty()) {
      forwardWithStatus(req, resp, "registration.jsp", "invalidName");
      return;
    }
    if (email == null || email.isEmpty()) {
      forwardWithStatus(req, resp, "registration.jsp", "invalidEmail");
      return;
    }
    if (password == null || password.isEmpty()) {
      forwardWithStatus(req, resp, "registration.jsp", "invalidPassword");
      return;
    }
    if (re_password == null || re_password.isEmpty() || !Objects.equals(password, re_password)) {
      forwardWithStatus(req, resp, "registration.jsp", "invalidRePassword");
      return;
    }
    if (phone == null || phone.isEmpty()) {
      forwardWithStatus(req, resp, "registration.jsp", "invalidPhone");
      return;
    } else if (phone.length() > 10) {
      forwardWithStatus(req, resp, "registration.jsp", "invalidPhoneLength");
      return;
    }

    userService.createNewUser(new UserDto(username, email, phone, password));

    req.setAttribute("status", "success");
    req.getRequestDispatcher("login.jsp").forward(req, resp);

    //    if (result > 0) {
    //      req.setAttribute("status", "success");
    //      dispatcher = req.getRequestDispatcher("login.jsp");
    //      dispatcher.forward(req, resp);
    //    } else {
    //      req.setAttribute("status", "failed");
    //      dispatcher = req.getRequestDispatcher("registration.jsp");
    //      dispatcher.forward(req, resp);
    //    }
  }

  private void forwardWithStatus(
      HttpServletRequest req, HttpServletResponse resp, String page, String status) {
    req.setAttribute("status", status);
    RequestDispatcher requestDispatcher = req.getRequestDispatcher(page);

    try {
      requestDispatcher.forward(req, resp);
    } catch (ServletException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
