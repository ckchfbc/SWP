/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import DB.DBConnection;
import Models.EventModels;
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
public class EventDAO {

    public boolean createEvent(EventModels event) {
        // Bắt đầu up ảnh
        String sql = "INSERT INTO events (event_name, event_details, image, date_start, date_end, event_status) "
                + "values (?, ?, ?, ?, ?, ?)";

        try ( Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, event.getEvent_name());
            stmt.setString(2, event.getEvent_details());
            if (event.getImage() != null) {
                // Gán giá trị file ảnh vào câu SQL
                stmt.setBlob(3, event.getImage());
            }
            stmt.setString(4, event.getDate_start());
            stmt.setString(5, event.getDate_end());
            stmt.setBoolean(6, event.isEvent_status());
            int row = stmt.executeUpdate();
            if (row > 0) {
                System.out.println("File uploaded and saved into database");
                return true;
            }

        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public static List<EventModels> getAllEvents() throws SQLException {
        String sql = "SELECT * FROM events;";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventModels> events = new ArrayList<>();
        try ( Connection conn = DBConnection.getConnection()) {
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                EventModels event = new EventModels();
                event.setEvent_id(rs.getInt("event_id"));
                event.setEvent_name(rs.getString("event_name"));
                event.setEvent_details(rs.getString("event_details"));
                event.setImage(null);
                event.setDate_start(rs.getString("date_start"));
                event.setDate_end(rs.getString("date_end"));

                events.add(event);  // Thêm event vào List
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return events;
    }
}