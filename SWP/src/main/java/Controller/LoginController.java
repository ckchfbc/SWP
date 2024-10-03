/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAOs.AccountDAO;
import GoogleClass.GoogleLogin;
import Models.CustomerAccountModel;
import Models.GoogleAccount;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        // Cho đăng ký
        if (state.equals("signup")) {
            String code = request.getParameter("code");
            GoogleLogin gg = new GoogleLogin();
            String accessToken = gg.getToken(code, state);
            GoogleAccount acc = gg.getUserInfo(accessToken, state);
            AccountDAO accDAO = new AccountDAO();
            if (accDAO.checkAccountExsit(acc.getEmail())) {
                String message = "Account already exists. Please log in.";
                // Set cái message thông bào nếu tài khoàn có tồn tại
                request.getSession().setAttribute("message", message);
                response.sendRedirect("/HomePageController/SignUp");
            } else {
                try {
                    if (accDAO.addNewAccountGoogle(acc)) {
                        if (accDAO.addNewCustomerAccountGoogle(acc)) {
                            String email = acc.getEmail();
                            Cookie userCookie = new Cookie("userEmail", email);
                            userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                            userCookie.setHttpOnly(true); // Bảo mật
                            userCookie.setPath("/");
                            response.addCookie(userCookie); // Thêm cookie vào phản hồi                
                            response.sendRedirect("/");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Lỗi add account google:\n" + e);
                    response.sendRedirect("/HomePageController/SignUp");
                }
            }
        }
        // Cho đăng nhập
        if (state.equals("login")) {
            String code = request.getParameter("code");
            GoogleLogin gg = new GoogleLogin();
            String accessToken = gg.getToken(code, state);
            GoogleAccount acc = gg.getUserInfo(accessToken, state);
            AccountDAO accDAO = new AccountDAO();
            if (!accDAO.checkAccountExsit(acc.getEmail())) {
                String message = "Account does not exist. Please register.";
                // Set cái message thông bào nếu tài khoàn có tồn tại
                request.getSession().setAttribute("message", message);
                response.sendRedirect("/HomePageController/Login");
            } else {
                String email = acc.getEmail();
                Cookie userCookie = new Cookie("userEmail", email);
                userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                userCookie.setHttpOnly(true); // Bảo mật
                userCookie.setPath("/");
                response.addCookie(userCookie); // Thêm cookie vào phản hồi                
                response.sendRedirect("/");
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
                    if (cookie.getName().equals("userEmail")) {
                        cookie.setMaxAge(0); // Xóa cookie bằng cách đặt thời gian sống là 0
                        response.addCookie(cookie); // Thêm lại cookie đã xóa vào phản hồi
                        break;
                    }
                }
            }
            response.sendRedirect("/"); // Chuyển hướng sau khi xóa cookie
        }

        if (request.getParameter("signUpBtn") != null) {
            String name = request.getParameter("nameTxt");
            String email = request.getParameter("emailTxt");
            String password = request.getParameter("pwdTxt");
            String a = request.getParameter("agreeBox");
            System.out.println(name + " " + email + " " + password + " " + a);
        }

        if (request.getParameter("loginBtn") != null) {
            String email = request.getParameter("emailTxt");
            String password = request.getParameter("pwdTxt");
            String a = request.getParameter("rememberBtn");
            if(!accDao.checkAccountExsit(email)){
                String message = "Incorrect Email or Password.";
                // Set cái message thông bào nếu tài khoàn ko tồn tại
                request.getSession().setAttribute("message", message);
                response.sendRedirect("/HomePageController/Login");
            }
        }
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
