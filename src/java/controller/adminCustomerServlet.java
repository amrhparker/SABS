package controller;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/adminCustomerServlet")
public class adminCustomerServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:derby://localhost:1527/SABSdb";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";

    // Handle registration and update (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String idStr = request.getParameter("id");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if ("update".equalsIgnoreCase(action) && idStr != null) {
                int id = Integer.parseInt(idStr);
                String sql = "UPDATE CUSTOMER SET NAME=?, EMAIL=?, PASSWORD=?, PHONE=? WHERE CUSTOMER_ID=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, password);
                    ps.setString(4, phone);
                    ps.setInt(5, id);
                    int rowsUpdated = ps.executeUpdate();
                    response.getWriter().write("Updated " + rowsUpdated + " row(s) successfully");
                }
            } else {
                String sql = "INSERT INTO CUSTOMER(NAME, EMAIL, PASSWORD, PHONE) VALUES(?,?,?,?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setString(3, password);
                    ps.setString(4, phone);
                    ps.executeUpdate();
                    response.getWriter().write("Customer registered successfully");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error: " + e.getMessage());
        }
    }

    // ✅ FIXED: Display all customers (GET) - Returns HTML table rows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT CUSTOMER_ID, NAME, EMAIL, PHONE FROM CUSTOMER ORDER BY CUSTOMER_ID";
            
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                StringBuilder html = new StringBuilder();
                boolean hasRows = false;

                while (rs.next()) {
                    hasRows = true;
                    int customerId = rs.getInt("CUSTOMER_ID");
                    String customerName = escapeHtml(rs.getString("NAME"));
                    String customerEmail = escapeHtml(rs.getString("EMAIL"));
                    String customerPhone = escapeHtml(rs.getString("PHONE"));
                    
                    html.append("<tr>");
                    html.append("<td>").append(customerId).append("</td>");
                    html.append("<td>").append(customerName).append("</td>");
                    html.append("<td>").append(customerEmail).append("</td>");
                    html.append("<td>");
                    html.append("<button onclick=\"viewCustomer('").append(customerId).append("', '").append(customerName.replace("'", "\\'")).append("')\">View</button> ");
                    html.append("<button onclick=\"editCustomer('").append(customerId).append("', '").append(customerName.replace("'", "\\'")).append("')\">Edit</button> ");
                    html.append("<button onclick=\"deleteCustomer('").append(customerId).append("', '").append(customerName.replace("'", "\\'")).append("')\">Delete</button>");
                    html.append("</td>");
                    html.append("</tr>");
                }

                // If no data was returned
                if (!hasRows) {
                    html.append("<tr><td colspan='4' class='loading'>No customers found in database</td></tr>");
                }

                response.getWriter().write(html.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("<tr><td colspan='4' class='error'>Error loading data: " + e.getMessage() + "</td></tr>");
        }
    }

    // ✅ FIXED: Get single customer by ID for modal (PUT) - Returns JSON
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.getWriter().write("{\"error\":\"No customer ID provided\"}");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "SELECT CUSTOMER_ID, NAME, EMAIL, PHONE, PASSWORD FROM CUSTOMER WHERE CUSTOMER_ID=?";
                
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        StringBuilder json = new StringBuilder();
                        json.append("{");
                        json.append("\"id\":").append(rs.getInt("CUSTOMER_ID")).append(",");
                        json.append("\"name\":\"").append(escapeJson(rs.getString("NAME"))).append("\",");
                        json.append("\"email\":\"").append(escapeJson(rs.getString("EMAIL"))).append("\",");
                        json.append("\"phone\":\"").append(escapeJson(rs.getString("PHONE"))).append("\",");
                        json.append("\"password\":\"").append(escapeJson(rs.getString("PASSWORD"))).append("\"");
                        json.append("}");
                        
                        response.getWriter().write(json.toString());
                    } else {
                        response.getWriter().write("{\"error\":\"Customer not found\"}");
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Invalid customer ID\"}");
        }
    }

    // DELETE customer
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.getWriter().write("0");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String sql = "DELETE FROM CUSTOMER WHERE CUSTOMER_ID=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, id);
                    int rowsDeleted = ps.executeUpdate();
                    response.getWriter().write(String.valueOf(rowsDeleted));
                }

            } catch (SQLException e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("0");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("0");
        }
    }

    // Helper method to escape JSON strings
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
    
    // Helper method to escape HTML
    private String escapeHtml(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#39;");
    }
}