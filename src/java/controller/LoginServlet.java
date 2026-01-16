package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        String table = "";  // choose DB table based on role
        
        if(role.equals("customer")) table = "customer";
        else if(role.equals("staff")) table = "staff";
        else if(role.equals("admin")) table = "admin";

        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/SABSdb", "app", "app"
            );

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM " + table + " WHERE email=? AND password=?"
            );

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                if(role.equals("admin")) {
                    String adminName = rs.getString("name");
                    HttpSession session = request.getSession();
                    session.setAttribute("adminName", adminName);
                } else if(role.equals("staff")) {
                    String staffName = rs.getString("name");
                    HttpSession session = request.getSession();
                    session.setAttribute("staffName", staffName);
                }
                response.getWriter().write("success");
            } else {
                response.getWriter().write("failed"); 
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("failed");
        }
    }
}
