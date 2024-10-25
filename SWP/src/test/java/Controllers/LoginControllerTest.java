package Controllers;

import DAOs.AccountDAO;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.eq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AccountDAO accountDAO;

    @Before
    public void setUp() {
        // Có thể thiết lập các giá trị mặc định nếu cần
    }

    @Test
    public void testLoginSuccessNormalUser() throws Exception {
        // Input
        when(request.getParameter("loginBtn")).thenReturn("loginBtn");
        when(request.getParameter("emailTxt")).thenReturn("kienlgce181396@fpt.edu.vn");
        when(request.getParameter("pwdTxt")).thenReturn("Lgk1412pvzz.");
        lenient().when(accountDAO.checkAccountExsit("kienlgce181396@fpt.edu.vn")).thenReturn(true);
        lenient().when(accountDAO.loginAccount("kienlgce181396@fpt.edu.vn", "Lgk1412pvzz.")).thenReturn(true);

        // Execute
        loginController.doPost(request, response);

        // Verify
        verify(response).sendRedirect("/");
    }

    @Test
    public void testLoginSuccessAdminUser() throws Exception {
        // Input
        when(request.getParameter("loginBtn")).thenReturn("loginBtn");
        when(request.getParameter("emailTxt")).thenReturn("admin@admin.com");
        when(request.getParameter("pwdTxt")).thenReturn("Admin1!");
        lenient().when(accountDAO.checkAccountExsit("admin@admin.com")).thenReturn(true);
        lenient().when(accountDAO.isAdmin("admin@admin.com")).thenReturn(true);
        lenient().when(accountDAO.loginAccount("admin@admin.com", "Admin1!")).thenReturn(true);

        // Execute
        loginController.doPost(request, response);

        // Verify
        verify(response).sendRedirect("/AdminController/Dashboard");
    }

    @Test
    public void testLoginAccountNotFound() throws Exception {
        // Mock HttpSession
        HttpSession session = mock(HttpSession.class);

        // Input
        when(request.getParameter("loginBtn")).thenReturn("loginBtn");
        when(request.getParameter("emailTxt")).thenReturn("notfound@example.com");
        when(request.getParameter("pwdTxt")).thenReturn("Password1!");
        lenient().when(accountDAO.checkAccountExsit("notfound@example.com")).thenReturn(false);
        
        // Khi gọi getSession(), trả về mock session
        when(request.getSession()).thenReturn(session);

        // Execute
        loginController.doPost(request, response);

        // Verify
        verify(response).sendRedirect("/HomePageController/Login");
        verify(session).setAttribute(eq("message"), anyString());
        verify(session).setAttribute("message", "Account does not exist. Please register.");
    }

    @Test
    public void testLoginIncorrectPassword() throws Exception {
        // Mock HttpSession
        HttpSession session = mock(HttpSession.class);

        // Input
        when(request.getParameter("loginBtn")).thenReturn("loginBtn");
        when(request.getParameter("emailTxt")).thenReturn("kienlgce181396@fpt.edu.vn");
        when(request.getParameter("pwdTxt")).thenReturn("WrongPassword!");
        lenient().when(accountDAO.checkAccountExsit("kienlgce181396@fpt.edu.vn")).thenReturn(true);
        lenient().when(accountDAO.loginAccount("kienlgce181396@fpt.edu.vn", "WrongPassword!")).thenReturn(false);
        
        // Khi gọi getSession(), trả về mock session
        when(request.getSession()).thenReturn(session);

        // Execute
        loginController.doPost(request, response);

        // Verify
        verify(response).sendRedirect("/HomePageController/Login");
        verify(session).setAttribute(eq("message"), anyString());
        verify(session).setAttribute("message", "Incorrect Password or Email.");
    }
}
