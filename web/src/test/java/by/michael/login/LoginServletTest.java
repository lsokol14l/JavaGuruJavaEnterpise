package by.michael.login;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class LoginServletTest {
    private LoginServlet servlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new LoginServlet();
    }

    @Test
    void testDoPost_EmptyEmail() throws Exception {
        when(request.getParameter("email")).thenReturn("");
        when(request.getParameter("password")).thenReturn("password");
        when(request.getRequestDispatcher("login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("status", "invalidEmail");
        verify(dispatcher).forward(request, response);
    }

    @Test
    void testDoPost_EmptyPassword() throws Exception {
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher("login.jsp")).thenReturn(dispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("status", "invalidPassword");
        verify(dispatcher).forward(request, response);
    }
}