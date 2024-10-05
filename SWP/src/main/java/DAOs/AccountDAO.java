/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Encryption.MD5;
import Models.AccountModel;
import Models.CustomerAccountModel;
import Models.GoogleAccount;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author thaii
 */
public class AccountDAO {

    public boolean checkAccountExsit(String acc_email) {
        String sql = "SELECT email FROM user_account where email=?";

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc_email);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String email = rs.getString("email");
                    if (acc_email.equals(email)) {
                        return true;
                    } else {
                        return false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addNewAccountGoogle(GoogleAccount acc) {
        String sql = "INSERT INTO user_account (email, role, date_created, status) VALUES (?, ?, ?, ?)";
        MD5 md5 = new MD5();
        String acc_email = acc.getEmail();
//        String passwordGoogle = md5.getMd5("google");
        String role = "customer";
        String date_created = String.valueOf(LocalDate.now());
        boolean status = true;

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, acc_email);
//            stmt.setString(2, passwordGoogle);
            stmt.setString(2, role);
            stmt.setString(3, date_created);
            stmt.setBoolean(4, status);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewCustomerAccountGoogle(GoogleAccount acc) {
        String sql = "SELECT * FROM user_account where email = ? and role = ? and status = true";
        String sql2 = "insert into customers (user_id, name, email, picture) values (?, ?, ?, ?)";
        String email = acc.getEmail();
        String name = acc.getName();
        String picture = acc.getPicture();
        String role = "customer";

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            stm.setString(2, role);
            PreparedStatement stm2 = conn.prepareStatement(sql2);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                stm2.setInt(1, rs.getInt("user_id"));
                stm2.setString(2, name);
                stm2.setString(3, email);
                stm2.setString(4, picture);
                stm2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public CustomerAccountModel getCustomerAccByEmail(GoogleAccount acc) {
        String sql = "select * from customers where email = ?";
        CustomerAccountModel cus_acc = null;
        String email = acc.getEmail();
        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int customer_id = rs.getInt("customer_id");
                int user_id = rs.getInt("user_id");
                String name = rs.getString("name");
                String picture = rs.getString("picture");
                String phone_number = rs.getString("phone_number");
                String cus_id_number = rs.getString("cus_id_number");
                String address = rs.getString("address");
                String preferred_contact_method = rs.getString("preferred_contact_method");
                cus_acc = new CustomerAccountModel(customer_id, user_id, name, email, picture, phone_number, cus_id_number, address, preferred_contact_method);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return cus_acc;
    }

    public boolean addNewAccount(String email, String name, String passwword) {
        String sql = "INSERT INTO user_account (email, password , role, date_created, status) VALUES (?, ?, ?, ?, ?)";
        MD5 md5 = new MD5();
        String hashPassword = md5.getMd5(passwword);
        String role = "customer";
        String date_created = String.valueOf(LocalDate.now());
        boolean status = true;

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, hashPassword);
            stmt.setString(3, role);
            stmt.setString(4, date_created);
            stmt.setBoolean(5, status);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addNewCustomerAccount(String email, String name) {
        String sql = "SELECT * FROM user_account where email = ? and role = ? and status = true";
        String sql2 = "insert into customers (user_id, name, email) values (?, ?, ?)";
        String role = "customer";

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stm = conn.prepareStatement(sql);
            stm.setString(1, email);
            stm.setString(2, role);
            PreparedStatement stm2 = conn.prepareStatement(sql2);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                stm2.setInt(1, rs.getInt("user_id"));
                stm2.setString(2, name);
                stm2.setString(3, email);
                stm2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean loginAccount(String email, String password) {
        String sql = "SELECT * FROM user_account where email=? and password = ?";
        MD5 md5 = new MD5();
        String hassPassword = md5.getMd5(password);
        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, hassPassword);

            try ( ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resetPassword(String email, String password) {
        String sql = "UPDATE user_account SET email = ?, password = ? WHERE email = ?;";
        MD5 md5 = new MD5();
        PreparedStatement stmt = null;
        String hassPassword = md5.getMd5(password);
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, hassPassword);
            stmt.setString(3, email);

            int rowsAffected = stmt.executeUpdate(); // Lưu số hàng bị ảnh hưởng
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
