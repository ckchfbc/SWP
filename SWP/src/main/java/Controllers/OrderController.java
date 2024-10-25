/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controllers;

import DAOs.OrderDAO;
import Models.OrderModel;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thaii
 */
public class OrderController extends HttpServlet {

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
            out.println("<title>Servlet OrderController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderController at " + request.getContextPath() + "</h1>");
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
        if (host.startsWith("/OrderController/Buy/")) {
            request.getRequestDispatcher("/views/order.jsp").forward(request, response);
        }
        if (host.startsWith("/OrderController/Edit/")) {
            request.getRequestDispatcher("/views/orderEdit.jsp").forward(request, response);
        }
        // Change deposit status
        if (host.startsWith("/OrderController/AcceptDeposit/")) {
            String[] s = host.split("/");
            String id = s[s.length - 1];

            OrderDAO orderDao = new OrderDAO();
            response.setContentType("text/html;charset=UTF-8");

            try ( PrintWriter out = response.getWriter()) {
                boolean isUpdated = orderDao.changeDepositStatus(id);
                if (isUpdated) {
                    out.println("<script>alert('Change Deposit Status Success!');</script>");
                    out.println("<script>window.close();</script>");
                } else {
                    out.println("<script>alert('Change Deposit Status Failed!');</script>");
                    out.println("<script>window.close();</script>");
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        // Change status order
        if (host.startsWith("/OrderController/AcceptOrder/")) {
            String[] s = host.split("/");
            String id = s[s.length - 1];

            OrderDAO orderDao = new OrderDAO();
            response.setContentType("text/html;charset=UTF-8");

            try ( PrintWriter out = response.getWriter()) {
                boolean isUpdated = orderDao.changeOrderStatus(id);
                if (isUpdated) {
                    out.println("<script>alert('Change Order Status Success!');</script>");
                    out.println("<script>window.close();</script>");
                } else {
                    out.println("<script>alert('Change Order Status Failed!');</script>");
                    out.println("<script>window.close();</script>");
                }
            } catch (SQLException ex) {
                Logger.getLogger(EventController.class.getName()).log(Level.SEVERE, null, ex);
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
        // khai báo all
        OrderDAO orderDao = new OrderDAO();
        if (request.getParameter("createOrder") != null) {
            int car_id = Integer.parseInt(request.getParameter("car_id"));
            int customer_id = Integer.parseInt(request.getParameter("customer_id"));
            String customer_cccd = request.getParameter("customer_cccd");
            String customer_phone = request.getParameter("customer_phone");
            String customer_address = request.getParameter("customer_address");
            int car_price = Integer.parseInt(request.getParameter("car_price"));
            BigDecimal price = BigDecimal.valueOf(car_price);

            try {
                orderDao.createOrder(car_id, customer_id, customer_cccd, customer_phone, customer_address, price);
            } catch (SQLException ex) {
                Logger.getLogger(OrderController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Lấy all orde cho thằng admin
        if (request.getParameter("fetchData") != null) {
            if (request.getParameter("fetchData").equals("true")) {

                List<OrderModel> orders = new ArrayList<>();
                try {
                    orders = orderDao.getAllOrders();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // Thiết lập kiểu phản hồi là JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Sử dụng Gson để chuyển danh sách thành JSON
                Gson gson = new Gson();
                String eventsJson = gson.toJson(orders);
                response.getWriter().write(eventsJson);
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
