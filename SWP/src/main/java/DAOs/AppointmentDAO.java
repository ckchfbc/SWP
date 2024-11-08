/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.AppointmentModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thaii
 */
public class AppointmentDAO {

    public List<AppointmentModel> getAppointment(int cus_id) {
        String sql = "SELECT * FROM appointments WHERE customer_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppointmentModel> appointments = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cus_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AppointmentModel appointment = new AppointmentModel();
                appointment.setAppointment_id(rs.getInt("appointment_id"));
                appointment.setCustomer_id(rs.getInt("customer_id"));
                appointment.setEmployee_id(rs.getInt("employee_id"));
                appointment.setDate_start(rs.getString("date_start"));
                appointment.setDate_end(rs.getString("date_end"));
                appointment.setNote(rs.getString("note"));
                appointment.setAppointment_status(rs.getBoolean("appointment_status"));

                appointments.add(appointment);  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public boolean checkHaveAppointment(int cus_id) {
        String sql = "SELECT customer_id, appointment_status AS status \n"
                + "FROM appointments \n"
                + "WHERE customer_id = ? \n"
                + "  AND appointment_status = false\n"
                + "\n"
                + "UNION \n"
                + "\n"
                + "SELECT customer_id, \n"
                + "       CASE \n"
                + "           WHEN deposit_status = false THEN 'Deposit Pending' \n"
                + "           ELSE 'Order Pending' \n"
                + "       END AS status \n"
                + "FROM orders \n"
                + "WHERE customer_id = ? \n"
                + "  AND (deposit_status = false OR order_status = false);";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cus_id);
            stmt.setInt(2, cus_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean createAppointment(String date_start, String note, int customer_id, String date_end) throws SQLException {
        String sql = "INSERT INTO appointments (customer_id, employee_id, date_start, date_end, note) \n"
                + "VALUES (?, \n"
                + "    (SELECT e.employee_id   -- Specify the table alias 'e' for employee_id\n"
                + "     FROM employees e\n"
                + "     LEFT JOIN appointments a ON e.employee_id = a.employee_id AND a.appointment_status = false\n"
                + "     GROUP BY e.employee_id\n"
                + "     ORDER BY COUNT(a.appointment_id) ASC\n"
                + "     LIMIT 1),\n"
                + "    ?, ?, ?\n"
                + ");";
        boolean check = checkHaveAppointment(customer_id);
        if (check) {
            return false;
        }
        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customer_id);
            stmt.setString(2, date_start);
            stmt.setString(3, date_end);
            stmt.setString(4, note);

            int row = stmt.executeUpdate();

            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean removeAppointment(int customer_id, int appointment_id) throws SQLException {
        String sql = "delete from appointments where customer_id = ? and appointment_id = ?;";

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customer_id);
            stmt.setInt(2, appointment_id);

            int row = stmt.executeUpdate();

            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<AppointmentModel> getAppointmentEmployee(int emp_id) {
        String sql = "SELECT * FROM appointments WHERE employee_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<AppointmentModel> appointments = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, emp_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                AppointmentModel appointment = new AppointmentModel();
                appointment.setAppointment_id(rs.getInt("appointment_id"));
                appointment.setCustomer_id(rs.getInt("customer_id"));
                appointment.setEmployee_id(rs.getInt("employee_id"));
                appointment.setDate_start(rs.getString("date_start"));
                appointment.setDate_end(rs.getString("date_end"));
                appointment.setNote(rs.getString("note"));
                appointment.setAppointment_status(rs.getBoolean("appointment_status"));

                appointments.add(appointment);  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    //change
    public boolean changeAppointment(int appointment_id) throws SQLException {
        String sql = "UPDATE appointments\n"
                + "SET appointment_status = true\n"
                + "WHERE appointment_id = ?;";

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, appointment_id);

            int row = stmt.executeUpdate();

            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
