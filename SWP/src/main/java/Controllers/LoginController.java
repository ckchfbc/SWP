/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.AccountDAO;
import GoogleClass.GoogleLogin;
import Models.AccountModel;
import Models.CustomerAccountModel;
import Models.GoogleAccount;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;

/**
 *
 * @author thaii
 */
public class LoginController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Kiểm tra điều kiện state coi là đăng ký hay đăng nhập
        String state = request.getParameter("state");
        // Cho đăng ký bằng Google
        if (state.equals("signup")) {
            String code = request.getParameter("code");
            GoogleLogin gg = new GoogleLogin();
            String accessToken = gg.getToken(code, state);
            GoogleAccount acc = gg.getUserInfo(accessToken, state);
            AccountDAO accDAO = new AccountDAO();
            if (accDAO.checkAccountExsit(acc.getEmail())) {
                String message = "Account already exists. Please log in.";
                sendMessageError(request, response, message, "/HomePageController/SignUp");
            } else {
                try {
                    if (accDAO.addNewAccountGoogle(acc)) {
                        if (accDAO.addNewCustomerAccountGoogle(acc)) {
                            String email = acc.getEmail();
                            setCookieLogin(request, response, email);
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Lỗi add account google:\n" + e);
                    response.sendRedirect("/HomePageController/SignUp");
                }
            }
        }
        // Cho đăng nhập bằng Google
        if (state.equals("login")) {
            String code = request.getParameter("code");
            GoogleLogin gg = new GoogleLogin();
            String accessToken = gg.getToken(code, state);
            GoogleAccount acc = gg.getUserInfo(accessToken, state);
            AccountDAO accDAO = new AccountDAO();
            if (!accDAO.checkAccountExsit(acc.getEmail())) {
                String message = "Account does not exist. Please register.";
                sendMessageError(request, response, message, "/HomePageController/Login");
            } else {
                String email = acc.getEmail();
               setCookieLogin(request, response, email);
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AccountDAO accDao = new AccountDAO();
        if (request.getParameter("logOut") != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    // Xóa cookie userEmail
                    if (cookie.getName().equals("userEmail")) {
                        cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống là 0
                        response.addCookie(cookie); // Thêm lại cookie đã xóa vào phản hồi
                    }

                    // Xóa cookie roleCookie
                    if (cookie.getName().equals("role")) {
                        cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống là 0
                        response.addCookie(cookie); // Thêm lại cookie đã xóa vào phản hồi
                    }
                }
            }
            response.sendRedirect("/"); // Chuyển hướng sau khi xóa cookie
        }

        // Đăng ký bình thường
        if (request.getParameter("signUpBtn") != null) {
            String name = request.getParameter("nameTxt");
            String email = request.getParameter("emailTxt");
            String password = request.getParameter("pwdTxt");
            String agree = request.getParameter("agreeBox");
            String currentDate = LocalDate.now().toString();
            String OTP = request.getParameter("OTPResult");
            String emailSendOTP = (String) request.getSession().getAttribute("emailSendOTP");
//            System.out.println("send otp: " + emailSendOTP);
//            System.out.println("email: " + email);
            if (!email.equals(emailSendOTP)) {
                String message = "The email sent does not match the verified email.";
                sendMessageError(request, response, message, "/HomePageController/SignUp");
                return;
            }

            if (OTP.equals("Success")) {
                if (agree != null) {
                    if (accDao.checkAccountExsit(email)) {
                        String message = "Account already exists. Please log in.";
                        sendMessageError(request, response, message, "/HomePageController/SignUp");
                    } else {
                        if (accDao.addNewAccount(email, name, password)) {
                            if (accDao.addNewCustomerAccount(email, name)) {
                                setCookieLogin(request, response, email);
                            }
                        }
                    }
                }
            } else {
                String message = "Please re-enter the OTP code to verify.";
                sendMessageError(request, response, message, "/HomePageController/Login");
            }
        }

        if (request.getParameter("loginBtn") != null) {
            String email = request.getParameter("emailTxt");
            String password = request.getParameter("pwdTxt");
            String a = request.getParameter("rememberBtn");
            if (!accDao.checkAccountExsit(email)) {
                String message = "Account does not exist. Please register.";
                sendMessageError(request, response, message, "/HomePageController/Login");
            } else {
                if (!accDao.isAdmin(email)) {
                    if (accDao.loginAccount(email, password)) {
                        setCookieLogin(request, response, email);
                    } else {
                        String message = "Incorrect Password or Email.";
                        sendMessageError(request, response, message, "/HomePageController/Login");
                    }
                } else {
                    if (accDao.loginAccount(email, password)) {
                        // Tạo một cookie admin mới
                        Cookie userCookie = new Cookie("admin", "true");
                        Cookie userCookie2 = new Cookie("email", email);
                        userCookie.setMaxAge(12 * 60 * 60); // 0.5ngày
                        userCookie.setHttpOnly(true); // Bảo mật
                        userCookie.setPath("/");
                        userCookie2.setMaxAge(12 * 60 * 60); // 0.5ngày
                        userCookie2.setHttpOnly(true); // Bảo mật
                        userCookie2.setPath("/");
                        response.addCookie(userCookie);
                        response.addCookie(userCookie2); 
                        response.sendRedirect("/AdminController/Dashboard");
                    } else {
                        String message = "Incorrect Password or Email.";
                        sendMessageError(request, response, message, "/HomePageController/Login");
                    }
                }

            }
        }

        // Admin log out
        if (null != request.getParameter("adminOut")) {
            if (request.getParameter("adminOut").equals("logOut")) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("admin")) {
                            cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống là 0
                            response.addCookie(cookie); // Thêm lại cookie đã xóa vào phản hồi
                        }
                        if (cookie.getName().equals("email")) {
                            cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống là 0
                            response.addCookie(cookie); // Thêm lại cookie đã xóa vào phản hồi
                        }
                    }
                }
            }
        }

    }

    public void setCookieLogin(HttpServletRequest request, HttpServletResponse response, String email) throws IOException {
        Cookie userCookie = new Cookie("userEmail", email);
        AccountDAO accDAO = new AccountDAO();
        String role = accDAO.getRole(email);
        Cookie roleCokie = new Cookie("role", role);
        //email
        userCookie.setMaxAge(24 * 60 * 60); // 7 ngày
        userCookie.setHttpOnly(true); // Bảo mật
        userCookie.setPath("/");
        response.addCookie(userCookie); // Thêm cookie vào phản hồi                
        //role
        roleCokie.setMaxAge(24 * 60 * 60); // 7 ngày
        roleCokie.setHttpOnly(true); // Bảo mật
        roleCokie.setPath("/");
        response.addCookie(roleCokie); // Thêm cookie vào phản hồi      
        response.sendRedirect("/");
    }

    public void sendMessageError(HttpServletRequest request, HttpServletResponse response, String message, String redirect) throws IOException {
        request.getSession().setAttribute("message", message);
        response.sendRedirect(redirect);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
