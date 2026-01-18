package controller;

import model.Appointment;
import util.DBConnection;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/CancelAppointmentServlet")
public class CancelAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* ================= SESSION CHECK ================= */
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customerId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String appointmentId = request.getParameter("appointmentId");
        Integer customerId = (Integer) session.getAttribute("customerId");

        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            response.sendRedirect("cancel.jsp");
            return;
        }

        /* ================= BUILD APPOINTMENT BEAN ================= */
        Appointment appt = new Appointment();
        appt.setAppointmentId(appointmentId);
        appt.setCustomerId(customerId);
        appt.setStatus("Cancelled");

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBConnection.getConnection();

            /* ================= UPDATE STATUS ================= */
            String sql
                    = "UPDATE appointment "
                    + "SET status = ? "
                    + "WHERE appointment_id = ? AND customer_id = ?";

            ps = conn.prepareStatement(sql);
            ps.setString(1, appt.getStatus());
            ps.setString(2, appt.getAppointmentId());
            ps.setInt(3, appt.getCustomerId());

            int updated = ps.executeUpdate();

            if (updated == 0) {
                // Appointment not found or not owned by user
                response.sendRedirect("cancel.jsp?error=invalid");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("cancel.jsp?error=db");
            return;

        } finally {
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

        /* ================= CLEAN SESSION & SUCCESS ================= */
        session.removeAttribute("cancelAppointmentId");
        response.sendRedirect("cancel.jsp?step=3");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("cancel.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Handles appointment cancellation using JavaBean";
    }
}
