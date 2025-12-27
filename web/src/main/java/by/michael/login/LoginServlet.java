package by.michael.login;

import by.michael.UserService;
import by.michael.dto.UserDto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
  UserService userService = new UserService();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // 1. Парсим данные с запроса
    String email = req.getParameter("email");
    String password = req.getParameter("password");

    // 2. Базовая валидация прежде чем отправлять запрос
    if (email == null || email.isEmpty()) {
      forwardWithStatus(req, resp, "login.jsp", "invalidEmail");
      return;
    }

    if (password == null || password.isEmpty()) {
      forwardWithStatus(req, resp, "login.jsp", "invalidPassword");
      return;
    }

    // Отправляем запрос к БД
    UserDto user = userService.authenticate(email, password);

    if (user != null) {
      HttpSession session = req.getSession();
      session.setAttribute("name", user.getName());
      session.setAttribute("email", user.getLogin());

      RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
      dispatcher.forward(req, resp);
    } else {
      forwardWithStatus(req, resp, "login.jsp", "failed");
    }
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
