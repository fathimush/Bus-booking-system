/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.busreservationsystem;

/**
 *
 * @author user
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusDAO {
    private Connection conn;

    public BusDAO(Connection conn) {
        this.conn = conn;
    }

    public int addBus(Bus bus) throws SQLException {
        String query = "INSERT INTO buses (busNumber, totalSeats, startPoint, endPoint, startTime, fare) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // Set the bus details in the query
            stmt.setInt(1, bus.busNumber);
            stmt.setInt(2, bus.totalSeats);
            stmt.setString(3, bus.startPoint);
            stmt.setString(4, bus.endPoint);
            stmt.setString(5, bus.startTime);
            stmt.setDouble(6, bus.fare);

            // Execute the query
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Retrieve and return the generated bus ID
                    }
                }
            }
        }
        return -1; // Return -1 in case of failure
    }
    
    public List<Bus> searchBuses(String startPoint, String endPoint) throws SQLException {
        String query = "SELECT * FROM buses WHERE startPoint = ? AND endPoint = ?";
        List<Bus> buses = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, startPoint);
            stmt.setString(2, endPoint);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    buses.add(new Bus(rs.getInt("busNumber"), rs.getInt("totalSeats"), rs.getString("startPoint"),
                            rs.getString("endPoint"), rs.getString("startTime"), rs.getDouble("fare")));
                }
            }
        }
        return buses;
    }

    public List<Bus> getAllBuses() throws SQLException {
        String query = "SELECT * FROM buses";
        List<Bus> buses = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    buses.add(new Bus(rs.getInt("busNumber"), rs.getInt("totalSeats"), rs.getString("startPoint"),
                            rs.getString("endPoint"), rs.getString("startTime"), rs.getDouble("fare")));
                }
            }
        }
        return buses;
    }
}
