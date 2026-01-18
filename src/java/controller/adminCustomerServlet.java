package controller; // your package

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

    // Handle registration (POST)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO CUSTOMER(NAME, EMAIL, PASSWORD) VALUES(?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.setString(4, phone);// ideally hash password before saving
                ps.executeUpdate();
            }
            response.sendRedirect("admin-customer.html"); // go back to page
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    // Display all customers (GET)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String sql = "SELECT CUSTOMER_ID, NAME, EMAIL FROM CUSTOMER";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ResultSet rs = ps.executeQuery();

                StringBuilder html = new StringBuilder();

                while (rs.next()) {
                    html.append("<tr>");
                    html.append("<td>").append(rs.getInt("CUSTOMER_ID")).append("</td>");
                    html.append("<td>").append(rs.getString("NAME")).append("</td>");
                    html.append("<td>").append(rs.getString("EMAIL")).append("</td>");
                    html.append("<td>");
                    html.append("<button onclick=\"openViewModal(")
                        .append(rs.getInt("CUSTOMER_ID"))
                        .append(")\">View</button>");
                    html.append("</td>");
                    html.append("</tr>");
                }

                response.getWriter().write(html.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("<tr><td colspan='4'>Error loading data</td></tr>");
        }
    }

    // Optional: get single customer by ID for modal
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.getWriter().write("{}");
            return;
        }

        int id = Integer.parseInt(idStr);

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String sql = "SELECT * FROM CUSTOMER WHERE CUSTOMER_ID=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String json = "{"
                            + "\"id\":" + rs.getInt("CUSTOMER_ID") + ","
                            + "\"name\":\"" + rs.getString("NAME") + "\","
                            + "\"email\":\"" + rs.getString("EMAIL") + "\""
                            + "}";
                    response.setContentType("application/json");
                    response.getWriter().write(json);
                } else {
                    response.getWriter().write("{}");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{}");
        }
    }
}
