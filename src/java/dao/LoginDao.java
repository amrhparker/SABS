package dao;

import model.Customer;
import db.DBConnection;

import java.sql.*;

public class LoginDao {

    public Customer login(String email, String password) {

        Customer customer = null;

        String sql
                = "SELECT customer_id, customer_name, email "
                + "FROM customer "
                + "WHERE email = ? AND password = ?";

        try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                customer = new Customer();
                customer.setCustomerId(rs.getInt("customer_id"));
                customer.setName(rs.getString("customer_name"));
                customer.setEmail(rs.getString("email"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return customer;
    }
}
