/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.ModelsCarModel;
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
public class ModelDAO {
    public static List<ModelsCarModel> getAllModels() throws SQLException {
        String sql = "SELECT * FROM model;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ModelsCarModel> models = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                ModelsCarModel model = new ModelsCarModel();
                model.setModel_id(rs.getInt("model_id"));
                model.setModel_name(rs.getString("model_name"));                      
                
                models.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return models;
    }
}
