package controller;

import model.Appointment;
import db.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;

@WebServlet("/AppointmentServlet")
public class AppointmentServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String searchQuery = request.getParameter("search");

        List<Appointment> appointments = new ArrayList<>();

        /* ================= FETCH DATA ================= */
        try (Connection conn = DBConnection.getConnection()) {

            String sql
                    = "SELECT a.appointment_id, a.customer_id, a.appointment_datetime, "
                    + "a.status, s.service_name "
                    + "FROM appointment a "
                    + "JOIN service s ON a.service_id = s.service_id ";

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                sql += "WHERE LOWER(a.appointment_id) LIKE ? "
                        + "OR LOWER(a.customer_id) LIKE ? ";
            }

            sql += "ORDER BY a.appointment_datetime DESC";

            PreparedStatement ps = conn.prepareStatement(sql);

            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String pattern = "%" + searchQuery.toLowerCase() + "%";
                ps.setString(1, pattern);
                ps.setString(2, pattern);
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment();
                appt.setAppointmentId(rs.getString("appointment_id"));
                appt.setCustomerId(rs.getString("customer_id"));
                appt.setAppointmentDatetime(rs.getTimestamp("appointment_datetime"));
                appt.setStatus(rs.getString("status"));

                // store service name temporarily
                request.setAttribute(
                        appt.getAppointmentId() + "_service",
                        rs.getString("service_name")
                );

                appointments.add(appt);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            out.println(
                    "<tr><td colspan='6' style='color:red; text-align:center;'>"
                    + "Error loading appointments"
                    + "</td></tr>"
            );
            return;
        }

        /* ================= OUTPUT ================= */
        if (appointments.isEmpty()) {
            out.println(
                    "<tr><td colspan='6' style='text-align:center;'>"
                    + "No appointments found"
                    + "</td></tr>"
            );
            return;
        }

        SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy | h:mm a");

        for (Appointment a : appointments) {

            String serviceName
                    = (String) request.getAttribute(a.getAppointmentId() + "_service");

            String status = a.getStatus();
            String statusClass = getStatusClass(status);

            boolean canAccept = "COMING SOON".equalsIgnoreCase(status);
            boolean canComplete = "ONGOING".equalsIgnoreCase(status);

            out.println("<tr>");
            out.println("<td>" + a.getAppointmentId() + "</td>");
            out.println("<td>" + a.getCustomerId() + "</td>");
            out.println("<td>" + fmt.format(a.getAppointmentDatetime()) + "</td>");
            out.println("<td>" + serviceName + "</td>");

            out.println(
                    "<td><span class='status-badge " + statusClass + "'>"
                    + status
                    + "</span></td>"
            );

            out.println("<td><div class='action-buttons'>");

            // Accept button
            if (canAccept) {
                out.println(
                        "<button class='action-btn accept' "
                        + "onclick=\"acceptAppointment('" + a.getAppointmentId() + "')\">"
                        + "Accept</button>"
                );
            } else {
                out.println("<button class='action-btn accept' disabled>Accept</button>");
            }

            // Complete button
            if (canComplete) {
                out.println(
                        "<button class='action-btn complete' "
                        + "onclick=\"completeAppointment('" + a.getAppointmentId() + "')\">"
                        + "Complete</button>"
                );
            } else {
                out.println("<button class='action-btn complete' disabled>Complete</button>");
            }

            out.println("</div></td>");
            out.println("</tr>");
        }
    }

    /* ================= STATUS BADGE ================= */
    private String getStatusClass(String status) {
        if (status == null) {
            return "";
        }
        switch (status.toUpperCase()) {
            case "COMING SOON":
                return "coming-soon";
            case "ONGOING":
                return "ongoing";
            case "COMPLETED":
                return "completed";
            case "CANCELLED":
                return "cancelled";
            default:
                return "";
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
