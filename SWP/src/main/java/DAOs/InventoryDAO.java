/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author thaii
 */
public class InventoryDAO {

    public boolean addInventory(int id, int quantity) throws SQLException {
        String sql = "INSERT INTO  inventory (car_id, quantity) values (?, ?)";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, quantity);
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean editInventory(int id, int quantity) throws SQLException {
        String sql = "UPDATE inventory SET quantity = ? WHERE car_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);            
            stmt.setInt(1, quantity);
            stmt.setInt(2, id);
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        }
        return false;
    }
}
