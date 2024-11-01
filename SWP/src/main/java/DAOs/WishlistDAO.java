/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.CarModel;
import Models.WishlistModel;
import Models.newCarModel;
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
public class WishlistDAO {

    public List<newCarModel> getAllWishlist(int cusId) throws SQLException {
        String sql = "SELECT \n"
                + "    c.*, \n"
                + "    i.quantity, \n"
                + "    (SELECT ci.car_image_id \n"
                + "     FROM car_image ci \n"
                + "     WHERE ci.car_id = c.car_id \n"
                + "     ORDER BY ci.car_image_id ASC \n"
                + "     LIMIT 1) AS first_car_image_id\n"
                + "FROM \n"
                + "    wishlist w \n"
                + "JOIN \n"
                + "    cars c ON w.car_id = c.car_id \n"
                + "LEFT JOIN \n"
                + "    inventory i ON c.car_id = i.car_id \n"
                + "WHERE \n"
                + "    w.customer_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<newCarModel> cars = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cusId);

            rs = stmt.executeQuery();
            while (rs.next()) {
                newCarModel car = new newCarModel();
                car.setCar_id(rs.getInt("car_id"));
                car.setBrand_id(rs.getInt("brand_id"));
                car.setModel_id(rs.getInt("model_id"));
                car.setCar_name(rs.getString("car_name"));
                car.setDate_start(rs.getString("date_start"));
                car.setColor(rs.getString("color"));
                car.setPrice(rs.getBigDecimal("price"));
                car.setFuel_id(rs.getInt("fuel_id"));
                car.setStatus(rs.getBoolean("status"));
                car.setDescription(rs.getString("description"));
                car.setQuantity(rs.getInt("quantity"));
                car.setFirst_car_image_id(rs.getInt("first_car_image_id"));
                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    // remove
    public boolean removeCarFromWishlist(int carId, int cusId) {
        String sql = "DELETE FROM wishlist WHERE car_id = ? AND customer_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carId);
            stmt.setInt(2, cusId);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // insert
    public boolean addWishlist(int carId, int cusId) {
        String sql = "INSERT INTO wishlist (car_id, customer_id) VALUES (?, ?);";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carId);
            stmt.setInt(2, cusId);
            int row = stmt.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkHaveWish(int carId, int cusId) {
        String sql = "select * from wishlist where car_id = ? AND customer_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, carId);
            stmt.setInt(2, cusId);
            ResultSet rs = stmt.executeQuery();
            int dem = 0;
            while(rs.next()){
                dem++;
                if(dem > 0){return dem > 0;}
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
