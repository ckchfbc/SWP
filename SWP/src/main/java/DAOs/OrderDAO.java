/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.CustomerAccountModel;
import Models.EmployeeModels;
import Models.OrderModel;
import java.math.BigDecimal;
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
public class OrderDAO {

    public boolean createOrder(int car_id, int customer_id, String customer_cccd, String customer_phone, String customer_address, BigDecimal car_price) throws SQLException {
        String selectEmployeeSql = "SELECT employee_id "
                + "FROM ( "
                + "    SELECT e.employee_id, COUNT(o.order_id) AS unfinished_orders "
                + "    FROM employees e "
                + "    LEFT JOIN orders o ON e.employee_id = o.employee_id "
                + "    AND o.order_status = false AND o.date_end > CURRENT_DATE() "
                + "    GROUP BY e.employee_id "
                + ") AS sub "
                + "ORDER BY unfinished_orders ASC, RAND() "
                + "LIMIT 1;";

        String insertOrderSql = "INSERT INTO orders (customer_id, employee_id, car_id, create_date, payment_method, total_amount, deposit_status, order_status, date_start, date_end) "
                + "VALUES (?, ?, ?, NOW(), 'online_transfer', ?, false, false, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY));";

        String updateCustomerSql = "UPDATE customers SET address = ?, phone_number = ?, cus_id_number = ? WHERE customer_id = ?;";

        try ( Connection conn = DBConnection.getConnection()) {
            // Lấy employee_id
            try ( PreparedStatement stmt = conn.prepareStatement(selectEmployeeSql)) {
                stmt.execute(); // Thực thi câu lệnh để lấy employee_id
            }

            // Thực hiện thêm đơn hàng
            try ( PreparedStatement stmt = conn.prepareStatement(insertOrderSql)) {
                // Retrieve employee_id based on the new selection logic
                PreparedStatement stmtSelect = conn.prepareStatement(selectEmployeeSql);
                ResultSet rs = stmtSelect.executeQuery();

                int employee_id = -1;
                if (rs.next()) {
                    employee_id = rs.getInt("employee_id");
                }

                // Now use employee_id in your insert statement
                PreparedStatement stmtInsert = conn.prepareStatement(insertOrderSql);
                stmtInsert.setInt(1, customer_id);
                stmtInsert.setInt(2, employee_id);
                stmtInsert.setInt(3, car_id);
                stmtInsert.setBigDecimal(4, car_price);

                // Execute the insert
                int rowsInserted = stmtInsert.executeUpdate();
                boolean isCreate = rowsInserted > 0;

                // Cập nhật thông tin khách hàng
                try ( PreparedStatement stmt2 = conn.prepareStatement(updateCustomerSql)) {
                    stmt2.setString(1, customer_address);
                    stmt2.setString(2, customer_phone);
                    stmt2.setString(3, customer_cccd);
                    stmt2.setInt(4, customer_id);
                    int row1 = stmt2.executeUpdate();
                    boolean isEdit = row1 > 0; // Kiểm tra xem có cập nhật thành công hay không

                    return isCreate && isEdit; // Trả về true nếu cả hai đều thành công
                }
            }
        }
    }

    public static List<OrderModel> getAllOrders() throws SQLException {
        String sql = "SELECT * FROM orders;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OrderModel> orders = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setOrder_id(rs.getInt("order_id"));
                order.setCustomer_id(rs.getInt("customer_id"));
                order.setEmployee_id(rs.getInt("employee_id"));
                order.setCar_id(rs.getInt("car_id"));
                order.setCreate_date(rs.getString("create_date"));
                order.setPayment_method(rs.getString("payment_method"));
                order.setTotal_amount(rs.getBigDecimal("total_amount"));
                order.setDeposit_status(rs.getBoolean("deposit_status"));
                order.setOrder_status(rs.getBoolean("order_status"));
                order.setDate_start(rs.getString("date_start"));
                order.setDate_end(rs.getString("date_end"));

                orders.add(order);  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public OrderModel getOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders where order_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderModel order = new OrderModel();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                order.setOrder_id(rs.getInt("order_id"));
                order.setCustomer_id(rs.getInt("customer_id"));
                order.setEmployee_id(rs.getInt("employee_id"));
                order.setCar_id(rs.getInt("car_id"));
                order.setCreate_date(rs.getString("create_date"));
                order.setPayment_method(rs.getString("payment_method"));
                order.setTotal_amount(rs.getBigDecimal("total_amount"));
                order.setDeposit_status(rs.getBoolean("deposit_status"));
                order.setOrder_status(rs.getBoolean("order_status"));
                order.setDate_start(rs.getString("date_start"));
                order.setDate_end(rs.getString("date_end"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public boolean changeDepositStatus(String id) throws SQLException {
        OrderModel event = getOrderById(Integer.parseInt(id));
        String sql = "UPDATE orders SET deposit_status = true WHERE order_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, event.getOrder_id());
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean changeOrderStatus(String id) throws SQLException {
        int orderId = Integer.parseInt(id);
        String sqlUpdateOrder = "UPDATE orders SET order_status = true WHERE order_id = ?;";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sqlUpdateOrder);
            stmt.setInt(1, orderId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                return updateInventory(orderId);
            }
        }

        return false;
    }

    public boolean updateInventory(int orderId) throws SQLException {
        String sqlUpdateInventory = "UPDATE inventory SET quantity = quantity - 1 WHERE car_id = (SELECT car_id FROM orders WHERE order_id = ?);";
        PreparedStatement stmt = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sqlUpdateInventory);
            stmt.setInt(1, orderId);
            int row = stmt.executeUpdate();
            if (row > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isOrderByCarAndCusForReview(int carId, int cusId) throws SQLException {
        String sql = "SELECT * FROM orders where car_id = ? and customer_id = ? and order_status = true;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        OrderModel order = new OrderModel();
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

    public static List<OrderModel> getAllOrdersForEmployee(String email) throws SQLException {
        EmployeeDAO empDao = new EmployeeDAO();
        EmployeeModels emp = empDao.getEmployeeByEmail(email);
        String sql = "SELECT \n"
                + "    o.*, \n"
                + "    w.warranty_id,\n"
                + "    CASE \n"
                + "        WHEN w.order_id IS NOT NULL THEN true \n"
                + "        ELSE false \n"
                + "    END AS has_warranty\n"
                + "FROM \n"
                + "    orders o\n"
                + "LEFT JOIN \n"
                + "    warranty w \n"
                + "ON \n"
                + "    o.order_id = w.order_id\n"
                + "WHERE \n"
                + "    o.employee_id = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OrderModel> orders = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, emp.getEmployeeId());
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setOrder_id(rs.getInt("order_id"));
                order.setCustomer_id(rs.getInt("customer_id"));
                order.setEmployee_id(rs.getInt("employee_id"));
                order.setCar_id(rs.getInt("car_id"));
                order.setCreate_date(rs.getString("create_date"));
                order.setPayment_method(rs.getString("payment_method"));
                order.setTotal_amount(rs.getBigDecimal("total_amount"));
                order.setDeposit_status(rs.getBoolean("deposit_status"));
                order.setOrder_status(rs.getBoolean("order_status"));
                order.setDate_start(rs.getString("date_start"));
                order.setDate_end(rs.getString("date_end"));
                order.setHas_warranty(rs.getBoolean("has_warranty"));
                order.setWarranty_id(rs.getInt("warranty_id"));

                orders.add(order);  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static List<OrderModel> getAllOrdersForCustomer(String email) throws SQLException {
        CustomerDAO cusDao = new CustomerDAO();
        CustomerAccountModel cus = cusDao.getCustomerInfor(email);
        int cus_id = cus.getCustomer_id();
        String sql = "select * from orders where customer_id = ?;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<OrderModel> orders = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cus_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                OrderModel order = new OrderModel();
                order.setOrder_id(rs.getInt("order_id"));
                order.setCustomer_id(rs.getInt("customer_id"));
                order.setEmployee_id(rs.getInt("employee_id"));
                order.setCar_id(rs.getInt("car_id"));
                order.setCreate_date(rs.getString("create_date"));
                order.setPayment_method(rs.getString("payment_method"));
                order.setTotal_amount(rs.getBigDecimal("total_amount"));
                order.setDeposit_status(rs.getBoolean("deposit_status"));
                order.setOrder_status(rs.getBoolean("order_status"));
                order.setDate_start(rs.getString("date_start"));
                order.setDate_end(rs.getString("date_end"));

                orders.add(order);  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orders;
    }

    public boolean checkHaveFiveNotDoneOrder(int cus_id) {
        String sql = "SELECT COUNT(*) as number FROM orders WHERE customer_id = ? AND (deposit_status = false OR (order_status = false AND date_end > CURRENT_DATE));";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cus_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int number = rs.getInt("number");
                return number >= 5;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Integer> getOrderIds(String email) throws SQLException {
        CustomerDAO cusDao = new CustomerDAO();
        CustomerAccountModel cus = cusDao.getCustomerInfor(email);
        int cus_id = cus.getCustomer_id();
        String sql = "SELECT order_id FROM orders WHERE customer_id = ? AND order_status = true AND deposit_status = true;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Integer> listOrderId = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cus_id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                listOrderId.add(rs.getInt("order_id"));  // Thêm event vào List
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listOrderId;
    }

    public boolean createOrderByEmp(int car_id, int customer_id, String customer_cccd, BigDecimal car_price, int emp_id) throws SQLException {
        String insertOrderSql = "INSERT INTO orders (customer_id, employee_id, car_id, create_date, payment_method, total_amount, deposit_status, order_status, date_start, date_end) "
                + "VALUES (?, ?, ?, NOW(), 'online_transfer', ?, false, false, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 7 DAY));";

        String updateCustomerSql = "UPDATE customers SET cus_id_number = ? WHERE customer_id = ?;";

        try ( Connection conn = DBConnection.getConnection()) {
            // Thực hiện thêm đơn hàng
            try ( PreparedStatement stmt = conn.prepareStatement(insertOrderSql)) {

                // Now use employee_id in your insert statement
                PreparedStatement stmtInsert = conn.prepareStatement(insertOrderSql);
                stmtInsert.setInt(1, customer_id);
                stmtInsert.setInt(2, emp_id);
                stmtInsert.setInt(3, car_id);
                stmtInsert.setBigDecimal(4, car_price);

                // Execute the insert
                int rowsInserted = stmtInsert.executeUpdate();
                boolean isCreate = rowsInserted > 0;

                // Cập nhật thông tin khách hàng
                try ( PreparedStatement stmt2 = conn.prepareStatement(updateCustomerSql)) {
                    stmt2.setString(1, customer_cccd);
                    stmt2.setInt(2, customer_id);
                    int row1 = stmt2.executeUpdate();
                    boolean isEdit = row1 > 0; // Kiểm tra xem có cập nhật thành công hay không

                    return isCreate && isEdit; // Trả về true nếu cả hai đều thành công
                }
            }
        }
    }
}
