/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.busreservationsystem;

import java.util.Stack;

/**
 *
 * @author user
 */
public class CustomerStack {
    private Stack<Customer> stack;

    public CustomerStack() {
        stack = new Stack<>();
    }
    // Method to add a customer to the stack
    public void push(Customer customer) {
        stack.push(customer);
    }
    // Method to remove the most recent customer (pop)
    public Customer pop() {
        if (!stack.isEmpty()) {
            return stack.pop();
        } else {
            System.out.println("No customers in the stack.");
            return null;
        }
    }
    // Method to display all customers in the stack (newest to oldest)
    public void displayCustomers() {
        if (stack.isEmpty()) {
            System.out.println("No customers to display.");
        } else {
            System.out.println("Displaying customers from newest to oldest:");
            // Create a temporary stack to print customers without removing them
            Stack<Customer> tempStack = (Stack<Customer>) stack.clone();
            while (!tempStack.isEmpty()) {
                Customer customer = tempStack.pop();
                System.out.println("ID: " + customer.id + ", Name: " + customer.name + ", Mobile: " + customer.mobile + ", Email: " + customer.email + ", City: " + customer.city + ", Age: " + customer.age);
            }
        }
    }
    // Method to check if the stack is empty
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}