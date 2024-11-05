/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author counh
 */
public class StaticDAO {

    public Map<String, Double> getDailyRevenue() {
        Map<String, Double> dailyRevenue = new HashMap<>();
        String query = "SELECT DATE(create_date) AS revenue_date, SUM(total_amount) AS total_revenue FROM orders GROUP BY DATE(create_date) ORDER BY revenue_date desc limit 10";

        try ( Connection connection = DBConnection.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(query);  ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String date = resultSet.getString("revenue_date");
                double revenue = resultSet.getDouble("total_revenue");
                dailyRevenue.put(date, revenue);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dailyRevenue;
    }

    public Map<String, String> getDailyCarStatic() {
        Map<String, String> DailyCarStatic = new HashMap<>();
        String query = "SELECT DATE(create_date) AS revenue_date, cars.car_name FROM orders JOIN cars ON cars.car_id = orders.car_id WHERE deposit_status = true AND order_status = true limit 10";

        try ( Connection connection = DBConnection.getConnection();  PreparedStatement preparedStatement = connection.prepareStatement(query);  ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String car_name = resultSet.getString("car_name");
                String revenue_date = resultSet.getString("revenue_date");
                DailyCarStatic.put(car_name, revenue_date);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return DailyCarStatic;
    }
}
