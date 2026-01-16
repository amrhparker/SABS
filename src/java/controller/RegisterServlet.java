package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.security.MessageDigest;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        String url = "jdbc:derby://localhost:1527/SABSdb";
        String dbUser = "app";
        String dbPassword = "app";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {

            String checkSql = "SELECT * FROM APP.CUSTOMER WHERE EMAIL = ?";
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, email);
                ResultSet rs = checkPs.executeQuery();
                if (rs.next()) {
                    response.sendRedirect("customerRegister.html?error=email_exists");
                    return;
                }
            }

            String insertSql = "INSERT INTO APP.CUSTOMER (NAME, EMAIL, PHONE, PASSWORD) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phone);
                ps.setString(4, password);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    response.sendRedirect("customerRegister.html?success=true");
                } else {
                    response.sendRedirect("customerRegister.html?success=false");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customerRegister.html?error=exception");
        }
    }
}
