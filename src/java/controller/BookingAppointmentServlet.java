package controller;

import model.Appointment;
import db.DBConnection;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/BookingAppointmentServlet")
public class BookingAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* ================= SESSION CHECK ================= */
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String customerId = (String) session.getAttribute("customerId");

        /* ================= FORM DATA ================= */
        String serviceId = request.getParameter("service"); // NOW service_id
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        
        if (serviceId == null || date == null || time == null
                || serviceId.isEmpty() || date.isEmpty() || time.isEmpty()) {
            response.sendRedirect("booking.jsp?error=true");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            /* ================= 2. GENERATE APPOINTMENT ID ================= */
            ps = conn.prepareStatement(
                    "SELECT MAX(SUBSTR(appointment_id, 4)) FROM appointment"
            );
            rs = ps.executeQuery();

            int next = 1;
            if (rs.next() && rs.getString(1) != null) {
                next = Integer.parseInt(rs.getString(1)) + 1;
            }

            String appointmentId = String.format("APP%04d", next);

            rs.close();
            ps.close();

            /* ================= 3. BUILD APPOINTMENT BEAN ================= */
            Timestamp appointmentDateTime
                    = Timestamp.valueOf(date + " " + time + ":00");

            Appointment appt = new Appointment();
            appt.setAppointmentId(appointmentId);
            appt.setAppointmentDatetime(appointmentDateTime);
            appt.setServiceId(serviceId);
            appt.setCustomerId(customerId);
            appt.setStatus("Coming Soon"); // IMPORTANT: consistent status

            /* ================= 4. INSERT APPOINTMENT ================= */
            ps = conn.prepareStatement(
                    "INSERT INTO appointment "
                    + "(appointment_id, appointment_datetime, service_id, status, customer_id) "
                    + "VALUES (?, ?, ?, ?, ?)"
            );

            ps.setString(1, appt.getAppointmentId());
            ps.setTimestamp(2, appt.getAppointmentDatetime());
            ps.setString(3, appt.getServiceId());
            ps.setString(4, appt.getStatus());
            ps.setString(5, appt.getCustomerId());

            ps.executeUpdate();

            /* ================= SUCCESS ================= */
            request.setAttribute("bookingSuccess", true);
            request.getRequestDispatcher("booking.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("booking.jsp?error=true");

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("booking.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Handles appointment booking";
    }
}
