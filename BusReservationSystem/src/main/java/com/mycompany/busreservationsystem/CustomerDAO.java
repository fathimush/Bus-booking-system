/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.busreservationsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author user
 */
public class CustomerDAO {
    private Connection conn;

    public CustomerDAO(Connection conn) {
        this.conn = conn;
    }

    public int registerCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customers (name, mobile, email, city, age) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, customer.name);
            stmt.setString(2, customer.mobile);
            stmt.setString(3, customer.email);
            stmt.setString(4, customer.city);
            stmt.setInt(5, customer.age);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        customer.id = rs.getInt(1); // Retrieve the generated customer ID
                    }
                }
                return customer.id;  // Return the generated ID
            } else {
                return -1; // Return -1 in case of failure
            }
        }
    }

    // Method to get all customers (newest to oldest)
    public CustomerStack getAllCustomers() throws SQLException {
        CustomerStack customerStack = new CustomerStack(); // Create a new stack to hold customers
        String query = "SELECT * FROM customers ORDER BY id DESC"; // Retrieve customers ordered by ID descending (newest first)
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String email = rs.getString("email");
                String city = rs.getString("city");
                int age = rs.getInt("age");

                Customer customer = new Customer( name, mobile, email, city, age);
                customerStack.push(customer); // Push the customer onto the stack
            }
        }
        return customerStack; // Return the stack of customers
    }
}