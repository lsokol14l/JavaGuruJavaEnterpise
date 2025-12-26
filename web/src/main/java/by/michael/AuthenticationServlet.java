package by.michael;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

@WebServlet("/authentification")
public class AuthenticationServlet extends HttpServlet {
  private UserService userService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    userService = new UserService();
  }

//  @Override
//  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//      throws ServletException, IOException {
//    String login = req.getParameter("login");
//    String password = req.getParameter("password");
//
//    userService.login(login, password)
//  }
}
