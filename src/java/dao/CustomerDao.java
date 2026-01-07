package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.Customer;          
import db.DBConnection;       

public class CustomerDao {

    // Login method
    public Customer login(String email, String password) {
        String sql = "SELECT * FROM customer WHERE email = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                return customer;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Register method 
    
    public boolean register(Customer customer) {
        String checkSql = "SELECT * FROM customer WHERE email = ?";
        String insertSql = "INSERT INTO customer (name, email, phone, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {

            // Check if email already exists
            checkPs.setString(1, customer.getEmail());
            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                return false; // Email already exists
            }

            // Insert new customer
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, customer.getName());
                ps.setString(2, customer.getEmail());
                ps.setString(3, customer.getPhone());
                ps.setString(4, customer.getPassword()); // plain text

                int rows = ps.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
