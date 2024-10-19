/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.CarModel;
import Models.newCarModel;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
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
public class CarDAO {

    public static List<CarModel> getAllCars() throws SQLException {
        String sql = "SELECT * FROM cars;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CarModel> cars = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                CarModel car = new CarModel();
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

                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public CarModel getCarById(int car_id) throws SQLException {
        String sql = "SELECT * FROM cars WHERE car_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CarModel car = new CarModel();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, car_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
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

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return car;
    }

    public boolean createCar(CarModel car) {
        String sql = "INSERT INTO cars (brand_id, model_id, car_name, date_start, color, price, fuel_id, status, description)\n"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, car.getBrand_id());
            stmt.setInt(2, car.getModel_id());
            stmt.setString(3, car.getCar_name());
            stmt.setString(4, car.getDate_start());
            stmt.setString(5, car.getColor());
            stmt.setBigDecimal(6, car.getPrice());
            stmt.setInt(7, car.getFuel_id());
            stmt.setBoolean(8, true);
            stmt.setString(9, car.getDescription());
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int findCarIdByName(CarModel car) {
        String sql = "SELECT car_id FROM cars where car_name = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, car.getCar_name());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("car_id");
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public boolean updateCarImage(Part file, int id) throws IOException {
        String sql = "UPDATE car_image SET picture = ? WHERE car_image_id = ?";
        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            InputStream inputStream = file.getInputStream();
            stmt.setBlob(1, inputStream);
            stmt.setInt(2, id);
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public boolean changeStatus(int id) throws SQLException {
        CarModel car = getCarById(id);
        String sql = "UPDATE cars SET status = ? WHERE car_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setBoolean(1, !car.isStatus());
            stmt.setInt(2, car.getCar_id());
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean editCar(CarModel car, int id) {
        String sql = "UPDATE cars SET brand_id = ?, model_id = ?, car_name = ?, date_start = ?, color = ?, price = ?, fuel_id = ?, description = ? WHERE car_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, car.getBrand_id());
            stmt.setInt(2, car.getModel_id());
            stmt.setString(3, car.getCar_name());
            stmt.setString(4, car.getDate_start());
            stmt.setString(5, car.getColor());
            stmt.setBigDecimal(6, car.getPrice());
            stmt.setInt(7, car.getFuel_id());
            stmt.setString(8, car.getDescription());
            stmt.setInt(9, id);

            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get new car
    public static List<newCarModel> getNewCars() throws SQLException {
        String sql = "SELECT c.*, \n"
                + "       (SELECT car_image_id \n"
                + "        FROM car_image ci \n"
                + "        WHERE ci.car_id = c.car_id \n"
                + "        ORDER BY ci.car_image_id ASC \n"
                + "        LIMIT 1) AS first_car_image_id\n"
                + "FROM cars c\n"
                + "WHERE c.status = 1\n"
                + "  AND c.date_start <= CURDATE()\n"
                + "ORDER BY c.date_start DESC, c.car_id DESC\n"
                + "LIMIT 4;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<newCarModel> newCars = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
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
                car.setFirst_car_image_id(rs.getInt("first_car_image_id"));
                newCars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newCars;
    }
}
