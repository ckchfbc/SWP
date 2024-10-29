/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.WarrantyModel;
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
public class WarrantyDAO {

    public boolean createWarranty(int order_id, String warranty_details, String warranty_expiry) {
        String sql = "INSERT INTO warranty (order_id, warranty_details, warranty_expiry) VALUES (?, ?, ?);";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, order_id);
            stmt.setString(2, warranty_details);
            stmt.setString(3, warranty_expiry);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public WarrantyModel getWarranty(int warranty_id) {
        String sql = "SELECT * FROM warranty where warranty_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, warranty_id);
            ResultSet rs = stmt.executeQuery();
            WarrantyModel warranty = new WarrantyModel();
            while (rs.next()) {
                warranty.setWarranty_id(rs.getInt("warranty_id"));
                warranty.setOrder_id(rs.getInt("order_id"));
                warranty.setWarranty_details(rs.getString("warranty_details"));
                warranty.setWarranty_expiry(rs.getString("warranty_expiry"));
            }
            return warranty;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean editWarranty(int warranty_id, String warranty_details, String warranty_expiry) {
        String sql = "UPDATE warranty SET warranty_details = ?, warranty_expiry = ? WHERE warranty_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, warranty_details);
            stmt.setString(2, warranty_expiry);
            stmt.setInt(3, warranty_id);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<WarrantyModel> getAllCustomerWarranty(int customer_id) {
        String sql = "SELECT w.* \n"
                + "FROM warranty w\n"
                + "JOIN orders o ON w.order_id = o.order_id\n"
                + "WHERE o.customer_id = ?;";
        PreparedStatement stmt = null;
        List<WarrantyModel> warranties = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, customer_id);
            ResultSet rs = stmt.executeQuery();
            WarrantyModel warranty = new WarrantyModel();
            while (rs.next()) {
                warranty.setWarranty_id(rs.getInt("warranty_id"));
                warranty.setOrder_id(rs.getInt("order_id"));
                warranty.setWarranty_details(rs.getString("warranty_details"));
                warranty.setWarranty_expiry(rs.getString("warranty_expiry"));
                
                warranties.add(warranty);
            }
            return warranties;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
