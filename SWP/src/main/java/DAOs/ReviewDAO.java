/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.ReviewModels;
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
public class ReviewDAO {

    public boolean checkCusHaveOrder(int carId, int cusId) throws SQLException {
        OrderDAO orderDao = new OrderDAO();
        boolean isCusHaveOrder = orderDao.isOrderByCarAndCusForReview(carId, cusId);
        boolean isHaveReview = checkHaveReview(carId, cusId);
        System.out.println(isCusHaveOrder);
        if (!isCusHaveOrder) {
            return true;
        }
        return isCusHaveOrder && isHaveReview;
    }

    public boolean checkHaveReview(int carId, int cusId) {
        String sql = "SELECT * FROM reviews where car_id = ? and customer_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carId);
            stmt.setInt(2, cusId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean createReview(int cusId, int carId, int rating, String review_text) {
        String sql = "INSERT INTO reviews (customer_id, car_id, rating, review_text, review_date, review_status)  VALUES (?, ?, ?, ?, CURRENT_DATE, ?); ";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cusId);
            stmt.setInt(2, carId);
            stmt.setInt(3, rating);
            stmt.setString(4, review_text);
            stmt.setBoolean(5, true);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<ReviewModels> getAllReviewOfCar(int carId) throws SQLException {
        String sql = "SELECT r.*, c.name FROM reviews r JOIN customers c ON r.customer_id = c.customer_id WHERE r.car_id = ? AND r.review_status = true;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ReviewModels> reviews = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ReviewModels review = new ReviewModels();
                review.setReview_id(rs.getInt("review_id"));
                review.setCustomer_id(rs.getInt("customer_id"));
                review.setCar_id(rs.getInt("car_id"));
                review.setRating(rs.getInt("rating"));
                review.setReview_text(rs.getString("review_text"));
                review.setReview_date(rs.getString("review_date"));
                review.setReview_status(rs.getBoolean("review_status"));
                review.setCustomer_name(rs.getString("name"));
                
                reviews.add(review);  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
