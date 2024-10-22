/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controller;

import DAOs.EmployeeDAO;
import Models.EmployeeModels;
import Models.EventModels;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet implementation class EmployeeController
 *
 * @author counh
 */
public class EmployeeController extends HttpServlet {

    private static final long serialVersionUID = 1L;

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
            out.println("<title>Servlet EmployeeController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EmployeeController at " + request.getContextPath() + "</h1>");
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
        System.out.println("Request URI: " + host);  // Debug: Check the request URI

        if (host.equals("/EmployeeController/Create")) {
            System.out.println("Navigating to createEvent.jsp");  // Debug
            request.getRequestDispatcher("/views/createEmployee.jsp").forward(request, response);
        } else if (host.startsWith("/EmployeeController/Edit")) {
            System.out.println("Navigating to editEvent.jsp");  // Debug
            request.getRequestDispatcher("/views/editEvent.jsp").forward(request, response);
        } else if (host.startsWith("/EmployeeController/Status/")) {
            String[] s = host.split("/");
            String id = s[s.length - 1];
            System.out.println("Extracted Employee ID: " + id);  // Debugging: Confirm ID extraction

            EmployeeDAO employeeDAO = new EmployeeDAO();
            response.setContentType("text/html;charset=UTF-8");

            try ( PrintWriter out = response.getWriter()) {
                boolean isUpdated = employeeDAO.changeStatus(id);
                System.out.println("Status Change Result: " + isUpdated);  // Debug: Check update result

                if (isUpdated) {
                    out.println("<script>window.close();</script>");  // Close window on success
                } else {
                    out.println("<script>alert('Failed to change status!');</script>");
                    out.println("<script>window.close();</script>");
                }
            } catch (SQLException ex) {
                System.out.println("SQL Exception: " + ex.getMessage());  // Debugging: Capture SQL error
                Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
                System.out.println("Unexpected Exception: " + e.getMessage());  // Debug unexpected errors
            }
        } else {
            System.out.println("No matching route found!");  // Debug
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
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

        EmployeeDAO employeeDAO = new EmployeeDAO();

        if (request.getParameter("createEmployee") != null) {
            // Nhận các tham số từ form
            String employeeName = request.getParameter("employee_name").trim();
            String employeeEmail = request.getParameter("employee_email");
            String employeePassword = request.getParameter("employee_password");
            String employeePhone = request.getParameter("employee_phone");

            boolean isCreated = employeeDAO.addNewEmployeeAccount(employeeName, employeeEmail, employeePassword, employeePhone);

            response.setContentType("text/html;charset=UTF-8");
            try ( PrintWriter out = response.getWriter()) {
                // Print parameter values to the output
                out.println("<html>");
                out.println("<body>");
                out.println("<h3>Received Parameters:</h3>");
                out.println("<p>createEmployee: " + request.getParameter("createEmployee") + "</p>");
                out.println("<p>employee_name: " + employeeName + "</p>");
                out.println("<p>employee_email: " + employeeEmail + "</p>");
                out.println("<p>employee_password: " + employeePassword + "</p>");
                out.println("<p>employee_phone: " + employeePhone + "</p>");

                if (isCreated) {
                    out.println("<p>Employee created successfully!</p>");
                    out.println("<script>window.close();</script>");
                } else {
                    out.println("<p>Failed to create employee.</p>");
                }

                out.println("</body>");
                out.println("</html>");
            }
        }

        // Handle AJAX request to fetch employee data
        String fetchData = request.getParameter("fetchData");

        if ("true".equals(fetchData)) {
            List<EmployeeModels> employees = new ArrayList<>();
            try {
                employees = employeeDAO.getAllEmployee(); // Retrieve the list of employees
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error fetching employees.");
                return;
            }

            // Set response type to JSON and encode in UTF-8
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convert employee list to JSON using Gson
            Gson gson = new Gson();
            String employeesJson = gson.toJson(employees);
            response.getWriter().write(employeesJson);
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
