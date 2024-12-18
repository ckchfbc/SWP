/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.AccountDAO;
import DAOs.CustomerDAO;
import DAOs.WishlistDAO;
import Models.CarModel;
import Models.CustomerAccountModel;
import Models.CustomerModel;
import Models.WishlistModel;
import Models.newCarModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thaii
 */
public class CustomerController extends HttpServlet {

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
        try ( PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet CustomerController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CustomerController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
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
        String host = request.getRequestURI();
        if (host.equals("/CustomerController/Profile")) {
            request.getRequestDispatcher("/views/profile.jsp").forward(request, response);
        }
        if (host.equals("/CustomerController/EditProfile")) {
            request.getRequestDispatcher("/views/editProfile.jsp").forward(request, response);
        }
        if (host.equals("/CustomerController/Warranty")) {
            request.getRequestDispatcher("/views/customerWarranty.jsp").forward(request, response);
        }
        if (host.equals("/CustomerController/Order")) {
            request.getRequestDispatcher("/views/orderCustomer.jsp").forward(request, response);
        }
        if (host.equals("/CustomerController/Feedback")) {
            request.getRequestDispatcher("/views/customerFeedback.jsp").forward(request, response);
        }
        if (host.equals("/CustomerController/Wishlist")) {
            request.getRequestDispatcher("/views/wishlist.jsp").forward(request, response);
        }
        if (host.equals("/CustomerController/Appointment")) {
            request.getRequestDispatcher("/views/appointmentCustomer.jsp").forward(request, response);
        }

        if (host.equals("/CustomerController/ResetPassword")) {
            request.getRequestDispatcher("/views/customerResetPwd.jsp").forward(request, response);
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
        // Khai báo DAO
        CustomerDAO cusDao = new CustomerDAO();

        // Get infor cho profile
        if (request.getParameter("getInforUser") != null) {
            String userEmail = request.getParameter("getInforUser");
            try {
                CustomerAccountModel cus = cusDao.getCustomerInfor(userEmail);
                
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(cus);

                // Gửi JSON phản hồi về client
                response.getWriter().write(jsonResponse);
            } catch (SQLException ex) {
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //editCusInfor
        if (request.getParameter("editCusInfor") != null) {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String oldEmail = request.getParameter("user_email");
            String address = null;
            String phone = null;
            String emailSendOTP = (String) request.getSession().getAttribute("emailSendOTP");

            if (!email.equals(emailSendOTP) && emailSendOTP != null) {
                String message = "The email sent does not match the verified email.";
                sendMessageError(request, response, message, "/CustomerController/EditProfile");
                return;
            }

            AccountDAO accDao = new AccountDAO();
            CustomerAccountModel cus = accDao.getCustomerAccByEmail(oldEmail);
            int cus_id = cus.getCustomer_id();
            int user_id = cus.getUser_id();

            if (request.getParameter("address") != null) {
                address = request.getParameter("address");
            }

            if (request.getParameter("phone") != null) {
                phone = request.getParameter("phone").trim();
            }

            boolean isUpdate = cusDao.updateCusotmerInfor(name, email, address, phone, cus_id, user_id);
            response.setContentType("text/html;charset=UTF-8");
            if (isUpdate) {
                changeCookie(request, response, email);
                try ( PrintWriter out = response.getWriter()) {
                    out.println("<script>");
                    out.println("alert('Success');");
                    out.println("window.close();");
                    out.println("</script>");
                }
            } else {
                try ( PrintWriter out = response.getWriter()) {
                    out.println("<script>");
                    out.println("alert('Error');");
                    out.println("window.close();");
                    out.println("</script>");
                }
            }
        }

        // Show infor cho edit profile
        if (request.getParameter("user_email") != null) {
            String user_email = request.getParameter("user_email");
            try {
                CustomerAccountModel cus = cusDao.getCustomerInfor(user_email);
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(cus);

                // Gửi JSON phản hồi về client
                response.getWriter().write(jsonResponse);
            } catch (SQLException ex) {
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //get cus for emp order
        if (request.getParameter("getCustomerInfo") != null) {
            int id = Integer.parseInt(request.getParameter("getCustomerInfo"));
            CustomerModel cus = cusDao.getCutomerById(id);

            // Set response type to JSON and encode in UTF-8
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convert employee list to JSON using Gson
            Gson gson = new Gson();
            String employeesJson = gson.toJson(cus);
            response.getWriter().write(employeesJson);
        }

        //get cus for emp order
        if (request.getParameter("getWishlistCars") != null && request.getParameter("getWishlistCars").equals("true")) {
            String email = request.getParameter("userEmail");
            CustomerAccountModel cus = new CustomerAccountModel();
            try {
                cus = cusDao.getCustomerInfor(email);
            } catch (SQLException ex) {
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            WishlistDAO wishDao = new WishlistDAO();
            List<newCarModel> wishlists = new ArrayList<>();
            try {
                wishlists = wishDao.getAllWishlist(cus.getCustomer_id());
            } catch (SQLException ex) {
                Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Set response type to JSON and encode in UTF-8
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convert employee list to JSON using Gson
            Gson gson = new Gson();
            String employeesJson = gson.toJson(wishlists);
            response.getWriter().write(employeesJson);
        }
    }

    public void sendMessageError(HttpServletRequest request, HttpServletResponse response, String message, String redirect) throws IOException {
        request.getSession().setAttribute("message", message);
        response.sendRedirect(redirect);
    }

    public void changeCookie(HttpServletRequest request, HttpServletResponse response, String newEmail) {
        Cookie[] cookies = request.getCookies();
        Cookie userEmailCookie = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userEmail")) {
                    userEmailCookie = cookie;
                    break;
                }
            }
        }

        if (userEmailCookie != null) {
            userEmailCookie.setValue(newEmail);
        } else {
            userEmailCookie = new Cookie("userEmail", newEmail);
        }
        response.addCookie(userEmailCookie);
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
