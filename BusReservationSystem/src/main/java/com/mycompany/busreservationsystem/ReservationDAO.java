/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.busreservationsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private Connection conn;

    public ReservationDAO(Connection conn) {
        this.conn = conn;
    }

    // Cancel Reservation
    public boolean cancelReservation(int reservationId) throws SQLException {
        String cancelQuery = "UPDATE Reservation SET status = 'CANCELED' WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(cancelQuery);
        stmt.setInt(1, reservationId);
        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            String waitingListQuery = """
                SELECT * FROM WaitingList WHERE busNumber = (
                    SELECT busNumber FROM Reservation WHERE id = ?
                ) ORDER BY reservationDate LIMIT 1
            """;
            PreparedStatement waitingStmt = conn.prepareStatement(waitingListQuery);
            waitingStmt.setInt(1, reservationId);
            ResultSet rs = waitingStmt.executeQuery();

            if (rs.next()) {
                int waitingId = rs.getInt("id");
                int customerId = rs.getInt("customerId");
                int busNumber = rs.getInt("busNumber");

                // Confirm the first waiting list reservation
                String confirmQuery = "INSERT INTO Reservation (customerId, busNumber, status) VALUES (?, ?, 'CONFIRMED')";
                PreparedStatement confirmStmt = conn.prepareStatement(confirmQuery);
                confirmStmt.setInt(1, customerId);
                confirmStmt.setInt(2, busNumber);
                confirmStmt.executeUpdate();

                // Remove from waiting list
                String removeQuery = "DELETE FROM WaitingList WHERE id = ?";
                PreparedStatement removeStmt = conn.prepareStatement(removeQuery);
                removeStmt.setInt(1, waitingId);
                removeStmt.executeUpdate();

                System.out.println("Reservation cancelled successfully. Next waiting list entry with ID " + waitingId + " has been confirmed.");
            } else {
                System.out.println("Reservation cancelled successfully. No customers on the waiting list.");
            }
            return true;
        }
        return false;
    }
    
    // Reserve a Seat
public boolean reserveSeat(int customerId, int busNumber) throws SQLException {
    // Check if seats are available
    String seatCheckQuery = "SELECT totalSeats - COUNT(*) AS availableSeats FROM Reservation WHERE busNumber = ? AND status = 'CONFIRMED'";
    PreparedStatement seatCheckStmt = conn.prepareStatement(seatCheckQuery);
    seatCheckStmt.setInt(1, busNumber);
    ResultSet seatCheckRs = seatCheckStmt.executeQuery();

    if (seatCheckRs.next()) {
        int availableSeats = seatCheckRs.getInt("availableSeats");

        if (availableSeats > 0) {
            // Reserve the seat
            String reserveQuery = "INSERT INTO Reservation (customerId, busNumber, status, reservationDate) VALUES (?, ?, 'CONFIRMED', CURRENT_TIMESTAMP)";
            PreparedStatement reserveStmt = conn.prepareStatement(reserveQuery);
            reserveStmt.setInt(1, customerId);
            reserveStmt.setInt(2, busNumber);
            reserveStmt.executeUpdate();

            System.out.println("Seat reserved successfully for customer ID: " + customerId + " on bus number: " + busNumber);
            return true;
        } else {
            System.out.println("No seats available on bus number: " + busNumber);
            return false;
        }
    }
    return false;
}


    // Request New Seat
    public void requestNewSeat(int customerId, int busNumber) throws SQLException {
        String query = "INSERT INTO WaitingList (customerId, busNumber) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, customerId);
        stmt.setInt(2, busNumber);
        stmt.executeUpdate();
        System.out.println("Customer added to the waiting list for bus number: " + busNumber);
    }

    // Display All Reservations
    public List<String> getAllReservations() throws SQLException {
        String query = "SELECT * FROM Reservation";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<String> reservations = new ArrayList<>();
        while (rs.next()) {
            reservations.add(String.format("Reservation{id=%d, customerId=%d, busNo=%d, status='%s', reservationDate=%s}",
                    rs.getInt("id"), rs.getInt("customerId"), rs.getInt("busNumber"), rs.getString("status"),
                    rs.getTimestamp("reservationDate")));
        }
        return reservations;
    }

    // Display Waiting List
    public List<String> getWaitingList() throws SQLException {
        String query = "SELECT * FROM WaitingList";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        List<String> waitingList = new ArrayList<>();
        while (rs.next()) {
            waitingList.add(String.format("Waiting{id=%d, customerId=%d, busNo=%d, reservationDate=%s}",
                    rs.getInt("id"), rs.getInt("customerId"), rs.getInt("busNumber"), rs.getTimestamp("reservationDate")));
        }
        return waitingList;
    }
}
