/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.busreservationsystem;

/**
 *
 * @author user
 */
public class Bus {
    int busNumber, totalSeats;
    String startPoint, endPoint, startTime;
    double fare;

    public Bus(int busNumber, int totalSeats, String startPoint, String endPoint, String startTime, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.startTime = startTime;
        this.fare = fare;
    }

    @Override
    public String toString() {
        return "Bus{busNumber=" + busNumber + ", totalSeats=" + totalSeats + ", startPoint='" + startPoint +
                "', endPoint='" + endPoint + "', startTime='" + startTime + "', fare=" + fare + "}";
    }
}
