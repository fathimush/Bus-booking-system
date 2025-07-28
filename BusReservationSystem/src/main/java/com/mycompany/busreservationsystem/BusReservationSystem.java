/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.busreservationsystem;

/**
 *
 * @author user
 */
import java.sql.*;
import java.util.Scanner;

public class BusReservationSystem {
    private static Connection conn;
    


    public static void main(String[] args) {
        try {
            // Database Connection
            conn = DatabaseConnection.getConnection();

            Scanner scanner = new Scanner(System.in);
            CustomerDAO customerDAO = new CustomerDAO(conn);
            BusDAO busDAO = new BusDAO(conn);
            ReservationDAO reservationDAO = new ReservationDAO(conn);

            while (true) {
                System.out.println("\nBus Reservation System Menu:");
                System.out.println("1. Register Customer");
                System.out.println("2. Register Bus");
                System.out.println("3. Search Buses");
                System.out.println("4. Display Bus List");
                System.out.println("5. Reserve a Seat");
                System.out.println("6. Display All Reservations");
                System.out.println("7. Cancel Reservation");
                System.out.println("8. Request a New Seat");
                System.out.println("9. Display Waiting List");
                System.out.println("10. Display Customers (Newest to Oldest)");
                System.out.println("11. Exit");

                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter mobile: ");
                        String mobile = scanner.nextLine();
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter city: ");
                        String city = scanner.nextLine();
                        System.out.print("Enter age: ");
                        int age = scanner.nextInt();

                        Customer customer = new Customer(name, mobile, email, city, age);
                        int customerId = customerDAO.registerCustomer(customer);

                        if (customerId > 0) {
                            System.out.println("Customer registered successfully.");
                            System.out.println("Customer Details: ");
                            System.out.println("ID: " + customerId + ", Name: " + name + ", Mobile: " + mobile + ", Email: " + email + ", City: " + city + ", Age: " + age);
                        } else {
                            System.out.println("Customer registration failed.");
                        }
                        break;

                    case 2:
                        System.out.print("Enter bus number: ");
                        int busNumber = scanner.nextInt();
                        System.out.print("Enter total seats: ");
                        int totalSeats = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        System.out.print("Enter start point: ");
                        String startPoint = scanner.nextLine();
                        System.out.print("Enter end point: ");
                        String endPoint = scanner.nextLine();
                        System.out.print("Enter start time (HH:mm): ");
                        String startTime = scanner.nextLine();
                        System.out.print("Enter fare: ");
                        double fare = scanner.nextDouble();

                        Bus bus = new Bus(busNumber, totalSeats, startPoint, endPoint, startTime, fare);
                        int busId = busDAO.addBus(bus);

                        if (busId > 0) {
                            System.out.println("Bus registered successfully.");
                            System.out.println("Bus Details: ");
                            System.out.println("ID: " + busId + ", Bus Number: " + busNumber + ", Total Seats: " + totalSeats + ", Start Point: " + startPoint + ", End Point: " + endPoint + ", Start Time: " + startTime + ", Fare: " + fare);
                        } else {
                            System.out.println("Bus registration failed.");
                        }
                        break;

                    case 3:
                        System.out.print("Enter start point: ");
                        String searchStartPoint = scanner.nextLine();
                        System.out.print("Enter end point: ");
                        String searchEndPoint = scanner.nextLine();
                        for (Bus b : busDAO.searchBuses(searchStartPoint, searchEndPoint)) {
                            System.out.println(b);
                        }
                        break;

                    case 4:
                        for (Bus b : busDAO.getAllBuses()) {
                            System.out.println(b);
                        }
                        break;

                    case 5:
                        System.out.print("Enter customer ID: ");
                        int customerIdForReservation = scanner.nextInt();
                        System.out.print("Enter bus number: ");
                        int reserveBusNumber = scanner.nextInt();

                        if (reservationDAO.reserveSeat(customerIdForReservation, reserveBusNumber)) {
                            System.out.println("Seat reserved for customer ID: " + customerIdForReservation + " on bus: " + reserveBusNumber);
                        } else {
                            System.out.println("Failed to reserve seat. Bus may be full.");
                        }
                        break;

                    case 6:
                        for (String reservation : reservationDAO.getAllReservations()) {
                            System.out.println(reservation);
                        }
                        break;

                    case 7:
                        System.out.print("Enter reservation ID to cancel: ");
                        int cancelReservationId = scanner.nextInt();
                        reservationDAO.cancelReservation(cancelReservationId);
                        break;

                    case 8:
                        System.out.print("Enter customer ID: ");
                        int waitCustomerId = scanner.nextInt();
                        System.out.print("Enter bus number: ");
                        int waitBusNumber = scanner.nextInt();
                        reservationDAO.requestNewSeat(waitCustomerId, waitBusNumber);
                        break;

                    case 9:
                        for (String waiting : reservationDAO.getWaitingList()) {
                            System.out.println(waiting);
                        }
                        break;
                    
                     case 10:
                        // Display all customers from newest to oldest
                        CustomerStack customerStack = customerDAO.getAllCustomers();
                        customerStack.displayCustomers();
                        break;
   

                    case 11:
                        System.out.println("Exiting the system. Goodbye!");
                        conn.close();
                        System.exit(0);

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
