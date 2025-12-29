package by.javaguru.git.mergeexperience;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/module4")
public class Module4Servlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Модуль 4";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");

        out.println("<a href=\"module3\"><- предыдущий модуль</a>");
        out.println("<br>");
        out.println("<a href=\"#\">следующий модуль -></a>");
    }

    public void destroy() {
    }
}