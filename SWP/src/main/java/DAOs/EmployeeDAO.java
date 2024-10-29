/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Encryption.MD5;
import Models.EmployeeModels;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 *
 * @author counh
 */
public class EmployeeDAO {

    // Add a new employee account with transaction management
    public boolean addNewEmployeeAccount(String email, String name, String password, String phoneNumber) {
        String accQuery = "INSERT INTO user_account (email, password, role, date_created, status) VALUES (?, ?, ?, ?, ?)";
        String empQuery = "INSERT INTO employees (user_id, name, email, phone_number) VALUES (?, ?, ?, ?)";
        String hashedPassword = md5Hex(password);
        String role = "employee";
        String dateCreated = LocalDate.now().toString();
        boolean status = true;

        try ( Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed!");
                return false;
            }

            conn.setAutoCommit(false); // Begin transaction

            try ( PreparedStatement accStmt = conn.prepareStatement(accQuery, Statement.RETURN_GENERATED_KEYS)) {
                accStmt.setString(1, email);
                accStmt.setString(2, hashedPassword);
                accStmt.setString(3, role);
                accStmt.setString(4, dateCreated);
                accStmt.setBoolean(5, status);

                if (accStmt.executeUpdate() > 0) {
                    try ( ResultSet keys = accStmt.getGeneratedKeys()) {
                        if (keys.next()) {
                            int userId = keys.getInt(1);

                            try ( PreparedStatement empStmt = conn.prepareStatement(empQuery)) {
                                empStmt.setInt(1, userId);
                                empStmt.setString(2, name);
                                empStmt.setString(3, email);
                                empStmt.setString(4, phoneNumber);

                                if (empStmt.executeUpdate() > 0) {
                                    conn.commit();
                                    System.out.println("Transaction committed successfully.");
                                    return true;
                                }
                            }
                        }
                    }
                }
                conn.rollback(); // Rollback transaction on failure
            }
        } catch (SQLException e) {
            System.err.println("Error adding new employee: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Update an employee account with optional password update
    public boolean editEmployeeAccount(int userId, String email, String name, String password, String phoneNumber) {
        boolean updatePassword = (password != null && !password.isEmpty());
        String accQuery = updatePassword
                ? "UPDATE user_account SET email = ?, password = ? WHERE user_id = ?"
                : "UPDATE user_account SET email = ? WHERE user_id = ?";

        String empQuery = "UPDATE employees SET name = ?, email = ?, phone_number = ? WHERE user_id = ?";

        try ( Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed!");
                return false;
            }

            conn.setAutoCommit(false); // Begin transaction

            try ( PreparedStatement accStmt = conn.prepareStatement(accQuery)) {
                accStmt.setString(1, email);
                if (updatePassword) {
                    accStmt.setString(2, md5Hex(password));
                    accStmt.setInt(3, userId);
                } else {
                    accStmt.setInt(2, userId);
                }
                accStmt.executeUpdate();
            }

            try ( PreparedStatement empStmt = conn.prepareStatement(empQuery)) {
                empStmt.setString(1, name);
                empStmt.setString(2, email);
                empStmt.setString(3, phoneNumber);
                empStmt.setInt(4, userId);
                empStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            System.out.println("Transaction committed successfully.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static List<EmployeeModels> getAllEmployee() throws SQLException {
        String sql = "SELECT e.*, ua.status FROM employees e join user_account ua on e.user_id = ua.user_id;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EmployeeModels> employees = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                EmployeeModels employee = new EmployeeModels();
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setUserId(rs.getInt("user_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setStatus(rs.getBoolean("status"));
                employees.add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }

    public EmployeeModels getEmployeeByID(int employeeID) throws SQLException {
        String sql = "SELECT e.*, ua.status FROM employees e join user_account ua on e.user_id = ua.user_id where e.employee_id =?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EmployeeModels employee = new EmployeeModels();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, employeeID);
            rs = stmt.executeQuery();
            while (rs.next()) {
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setUserId(rs.getInt("user_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setStatus(rs.getBoolean("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }

    public boolean changeStatus(String id) throws SQLException {
        EmployeeModels employee = getEmployeeByID(Integer.parseInt(id));
        String sql = "UPDATE swp.user_account AS ua "
                + "JOIN swp.employees AS e ON ua.user_id = e.user_id "
                + "SET ua.status = ? "
                + "WHERE e.employee_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            if (employee.isStatus() != true) {
                stmt.setBoolean(1, !employee.isStatus());
                stmt.setInt(2, employee.getEmployeeId());
                int row = stmt.executeUpdate();
                if (row > 0) {
                    return true;
                }
            } else {
                stmt.setBoolean(1, !employee.isStatus());
                stmt.setInt(2, employee.getEmployeeId());
                int row = stmt.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public EmployeeModels getEmployeeByEmail(String email) throws SQLException {
        String sql = "SELECT e.*, ua.status FROM employees e join user_account ua on e.user_id = ua.user_id where e.email =?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EmployeeModels employee = new EmployeeModels();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                employee.setEmployeeId(rs.getInt("employee_id"));
                employee.setUserId(rs.getInt("user_id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPhoneNumber(rs.getString("phone_number"));
                employee.setStatus(rs.getBoolean("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employee;
    }
}
