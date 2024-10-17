/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.FuelModel;
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
public class FuelDAO {
    public static List<FuelModel> getAllFuels() throws SQLException {
        String sql = "SELECT * FROM fuel;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<FuelModel> fuels = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                FuelModel fuel = new FuelModel();
                fuel.setFuel_id(rs.getInt("fuel_id"));
                fuel.setFuel_name(rs.getString("fuel_name"));                      
                
                fuels.add(fuel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fuels;
    }
}
