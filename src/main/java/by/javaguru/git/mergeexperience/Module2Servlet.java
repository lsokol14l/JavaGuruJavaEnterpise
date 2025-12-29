package by.javaguru.git.mergeexperience;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/module2")
public class Module2Servlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Модуль 2";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");

        out.println("<a href=\"module1\"><- предыдущий модуль</a>");
        out.println("<br>");
        out.println("<a href=\"module3\">следующий модуль -></a>");

        out.println("</body></html>");
    }

    public void destroy() {
    }
}