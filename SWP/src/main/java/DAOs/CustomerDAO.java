/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.CustomerAccountModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author thaii
 */
public class CustomerDAO {

    public static CustomerAccountModel getCustomerInfor(String email) throws SQLException {
        String sql = "SELECT * FROM customers WHERE email = ?";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        CustomerAccountModel cus = new CustomerAccountModel();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            while (rs.next()) {
                cus.setUser_id(rs.getInt("user_id"));
                cus.setCustomer_id(rs.getInt("customer_id"));
                cus.setName(rs.getString("name"));
                cus.setEmail(rs.getString("email"));
                cus.setPicture(rs.getString("picture"));
                cus.setPhone_number(rs.getString("phone_number"));
                cus.setCus_id_number(rs.getString("cus_id_number"));
                cus.setAddress(rs.getString("address"));
                cus.setPreferred_contact_method(rs.getString("preferred_contact_method"));

                return cus;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateCusotmerInfor(String name, String email, String address, String phone, int cus_id) {
        String sql1 = "UPDATE customers SET name = ?, email = ?, address = ?, phone_number = ? WHERE customer_id = ?; ";
        String sql2 = "UPDATE customers SET name = ?, email = ? WHERE customer_id = ?; ";
        String sql3 = "UPDATE customers SET name = ?, email = ?, phone_number = ? WHERE customer_id = ?; ";
        String sql4 = "UPDATE customers SET name = ?, email = ?, address = ? WHERE customer_id = ?; ";
        PreparedStatement stmt = null;
        System.out.println(name + " " + email + " " + address + " " + phone.length() + " " + cus_id);
        //Everything ko null
        if (address != null && phone != null) {
            try ( Connection conn = DBConnection.getConnection()) {
                stmt = conn.prepareStatement(sql1);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, address);
                stmt.setString(4, phone);
                stmt.setInt(5, cus_id);
                int row = stmt.executeUpdate();
                return (row > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Only address ko null
        if (address != null && phone == null) {
            try ( Connection conn = DBConnection.getConnection()) {
                stmt = conn.prepareStatement(sql4);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, address);
                stmt.setInt(4, cus_id);
                int row = stmt.executeUpdate();
                return (row > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Only phone ko null
        if (address == null && phone != null) {
            try ( Connection conn = DBConnection.getConnection()) {
                stmt = conn.prepareStatement(sql3);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setInt(4, cus_id);
                int row = stmt.executeUpdate();
                return (row > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //2 cái null hết
        if (address == null && phone == null) {
            try ( Connection conn = DBConnection.getConnection()) {
                stmt = conn.prepareStatement(sql2);
                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setInt(3, cus_id);
                int row = stmt.executeUpdate();
                return (row > 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
}
