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
        String sql = "SELECT c.*, i.quantity FROM cars c LEFT JOIN inventory i ON c.car_id = i.car_id; ";
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
                car.setQuantity(rs.getInt("quantity"));

                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public CarModel getCarById(int car_id) throws SQLException {
        String sql = "SELECT c.*, i.quantity\n"
                + "FROM cars c\n"
                + "LEFT JOIN inventory i ON c.car_id = i.car_id\n"
                + "WHERE c.car_id = ?;";
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
                car.setQuantity(rs.getInt("quantity"));
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
                + "        LIMIT 1) AS first_car_image_id,\n"
                + "       i.quantity\n"
                + "FROM cars c\n"
                + "LEFT JOIN inventory i ON c.car_id = i.car_id\n"
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
                car.setQuantity(rs.getInt("quantity"));
                newCars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newCars;
    }

    public static List<CarModel> getRelatedCar(int brand_id) throws SQLException {
        String sql = "SELECT c.*, i.quantity\n"
                + "FROM cars c\n"
                + "LEFT JOIN inventory i ON c.car_id = i.car_id\n"
                + "WHERE c.brand_id = ? AND c.status = true\n"
                + "ORDER BY RAND()\n"
                + "LIMIT 4;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<CarModel> cars = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, brand_id);
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
                car.setQuantity(rs.getInt("quantity"));

                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    // Get best sell car
    public static List<newCarModel> getBestSellingCars() throws SQLException {
        String sql = "SELECT \n"
                + "    c.*,  \n"
                + "    MAX(i.quantity) AS quantity, \n"
                + "    COUNT(o.car_id) AS total_orders, \n"
                + "    (\n"
                + "        SELECT car_image_id \n"
                + "        FROM car_image ci \n"
                + "        WHERE ci.car_id = c.car_id \n"
                + "        ORDER BY ci.car_image_id ASC \n"
                + "        LIMIT 1\n"
                + "    ) AS first_car_image_id \n"
                + "FROM \n"
                + "    cars c \n"
                + "LEFT JOIN \n"
                + "    inventory i ON c.car_id = i.car_id \n"
                + "LEFT JOIN \n"
                + "    orders o ON c.car_id = o.car_id \n"
                + "WHERE \n"
                + "    c.status = 1 \n"
                + "    AND c.date_start <= CURDATE() \n"
                + "GROUP BY \n"
                + "    c.car_id \n"
                + "ORDER BY \n"
                + "    total_orders DESC \n"
                + "LIMIT 12;";
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
                car.setQuantity(rs.getInt("quantity"));
                newCars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newCars;
    }
// Method to get car details by name with partial matching

    public List<newCarModel> getCarByName(String input) {
        String sql = "SELECT c.*, "
                + "(SELECT car_image_id FROM car_image ci WHERE ci.car_id = c.car_id ORDER BY ci.car_image_id ASC LIMIT 1) AS first_car_image_id, "
                + "i.quantity "
                + "FROM cars c "
                + "LEFT JOIN inventory i ON c.car_id = i.car_id "
                + "WHERE c.status = 1 AND c.car_name LIKE ?;";

        List<newCarModel> newCars = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the parameter with wildcard for partial matching
            stmt.setString(1, "%" + input + "%");
            ResultSet rs = stmt.executeQuery();

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
                car.setQuantity(rs.getInt("quantity"));

                newCars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newCars;
    }

    public List<CarModel> getListAllCars() throws SQLException {
        String sql = "SELECT c.*, i.quantity FROM cars c LEFT JOIN inventory i ON c.car_id = i.car_id; ";
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
                car.setQuantity(rs.getInt("quantity"));

                cars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cars;
    }

    public List<newCarModel> getListtAllCars() {
        String sql = "SELECT c.*, "
                + "(SELECT car_image_id FROM car_image ci WHERE ci.car_id = c.car_id ORDER BY ci.car_image_id ASC LIMIT 1) AS first_car_image_id, "
                + "i.quantity "
                + "FROM cars c "
                + "LEFT JOIN inventory i ON c.car_id = i.car_id "
                + "WHERE c.status = 1;";

        List<newCarModel> newCars = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

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
                car.setQuantity(rs.getInt("quantity"));

                newCars.add(car);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newCars;
    }

    // Method to get all brands
    public List<String> getAllBrands() {
        List<String> brands = new ArrayList<>();
        String sql = "SELECT brand_name FROM brands";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                brands.add(rs.getString("brand_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching brands: " + e.getMessage());
        }
        return brands;
    }

// Method to get all fuel types
    public List<String> getAllFuelTypes() {
        List<String> fuels = new ArrayList<>();
        String sql = "SELECT fuel_name FROM fuel;";

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql);  ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                fuels.add(rs.getString("fuel_name"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching fuel types: " + e.getMessage());
        }
        return fuels;
    }

// Method to filter cars by price, brand, or fuel
    public List<newCarModel> getFilteredCars(String price, String brand, String fuel) {
        List<newCarModel> cars = new ArrayList<>();

        // Base SQL query
        String sql = "SELECT c.car_id, c.car_name, b.brand_name, b.brand_id, f.fuel_name, f.fuel_id, "
                + "c.price, c.color, c.date_start, c.status, c.description, i.quantity, "
                + "c.first_car_image_id FROM cars c "
                + "JOIN brands b ON c.brand_id = b.brand_id "
                + "JOIN fuel f ON c.fuel_id = f.fuel_id "
                + "JOIN inventory i ON c.car_id = i.car_id WHERE 1=1 ";

        // Add dynamic filters based on input parameters
        if (price != null && !price.isEmpty()) {
            sql += getPriceFilter(price);
        }
        if (brand != null && !brand.isEmpty()) {
            sql += "AND b.brand_name = ? ";
        }
        if (fuel != null && !fuel.isEmpty()) {
            sql += "AND f.fuel_name = ? ";
        }

        try ( Connection conn = DBConnection.getConnection();  PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters dynamically
            int paramIndex = 1;
            if (brand != null && !brand.isEmpty()) {
                stmt.setString(paramIndex++, brand);
            }
            if (fuel != null && !fuel.isEmpty()) {
                stmt.setString(paramIndex++, fuel);
            }

            // Execute query and populate the list
            try ( ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    newCarModel car = new newCarModel();
                    car.setCar_id(rs.getInt("car_id"));
                    car.setCar_name(rs.getString("car_name"));
                    car.setBrand_id(rs.getInt("brand_id"));
                    car.setFuel_id(rs.getInt("fuel_id"));
                    car.setPrice(rs.getBigDecimal("price"));
                    car.setColor(rs.getString("color"));
                    car.setDate_start(rs.getString("date_start"));
                    car.setStatus(rs.getBoolean("status"));
                    car.setDescription(rs.getString("description"));
                    car.setFirst_car_image_id(rs.getInt("first_car_image_id"));
                    car.setQuantity(rs.getInt("quantity"));

                    cars.add(car);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error filtering cars: " + e.getMessage());
        }

        return cars;
    }
// Helper method to generate SQL condition for price filter

    private String getPriceFilter(String price) {
        switch (price) {
            case "under-20000":
                return "AND c.price < 20000 ";
            case "20000-50000":
                return "AND c.price BETWEEN 20000 AND 50000 ";
            case "above-50000":
                return "AND c.price > 50000 ";
            default:
                System.err.println("Invalid price range: " + price);
                return "";
        }
    }
}
