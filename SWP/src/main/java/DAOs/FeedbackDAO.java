/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.FeedbackModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thaii
 */
public class FeedbackDAO {

    public boolean createFeeadback(int order_id, String feedbackContent, int customerId) {
        String sql = "INSERT INTO feedbacks (order_id, customer_id, feedback_content, date_create, feedback_status) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement stmt = null;
        // Lấy ngày hiện tại
        LocalDate currentDate = LocalDate.now();

        // Định dạng ngày theo kiểu yyyy-MM-dd
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        boolean isHaveFeedback = checkHaveFeedbackOnOrder(order_id, customerId);

        if (isHaveFeedback) {
            return false;
        }

        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order_id);
            stmt.setInt(2, customerId);
            stmt.setString(3, feedbackContent);
            stmt.setString(4, formattedDate);
            stmt.setBoolean(5, false);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkHaveFeedbackOnOrder(int order_id, int customerId) {
        String sql = "SELECT * FROM feedbacks where order_id = ? AND feedback_status = false AND customer_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order_id);
            stmt.setInt(2, customerId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<FeedbackModel> getAllReviewForCustomer(int cusId) throws SQLException {
        String sql = "SELECT * FROM feedbacks where customer_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<FeedbackModel> fbs = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cusId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                FeedbackModel fb = new FeedbackModel();
                fb.setFeedback_id(rs.getInt("feedback_id"));
                fb.setOrder_id(rs.getInt("order_id"));
                fb.setCustomer_id(rs.getInt("customer_id"));
                fb.setFeedback_content(rs.getString("feedback_content"));
                fb.setDate_create(rs.getString("date_create"));
                fb.setFeedback_status(rs.getBoolean("feedback_status"));

                fbs.add(fb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fbs;
    }

    public static List<FeedbackModel> getAllReviewForEmployee(int cusId) throws SQLException {
        String sql = "SELECT f.* FROM feedbacks f JOIN orders o ON f.order_id = o.order_id WHERE o.employee_id = 4;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<FeedbackModel> fbs = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cusId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                FeedbackModel fb = new FeedbackModel();
                fb.setFeedback_id(rs.getInt("feedback_id"));
                fb.setOrder_id(rs.getInt("order_id"));
                fb.setCustomer_id(rs.getInt("customer_id"));
                fb.setFeedback_content(rs.getString("feedback_content"));
                fb.setDate_create(rs.getString("date_create"));
                fb.setFeedback_status(rs.getBoolean("feedback_status"));

                fbs.add(fb);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fbs;
    }

    //Change status
    public boolean changeStatus(int fbid) {
        String sql = "UPDATE feedbacks  SET feedback_status = true  WHERE feedback_id = ?; ";
        PreparedStatement stmt = null;
        List<FeedbackModel> fbs = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fbid);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
