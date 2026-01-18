package controller;

import model.Appointment;
import db.DBConnection;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CompleteAppointmentServlet")
public class CompleteAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentId = request.getParameter("id");

        /* ================= BASIC VALIDATION ================= */
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            /* ================= 1. LOAD APPOINTMENT ================= */
            String checkSql
                    = "SELECT status FROM appointment WHERE appointment_id = ?";

            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, appointmentId);
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            /* ================= 2. BUILD APPOINTMENT BEAN ================= */
            Appointment appt = new Appointment();
            appt.setAppointmentId(appointmentId);
            appt.setStatus(rs.getString("status"));

            /* ================= 3. STATUS VALIDATION ================= */
            if (!"ONGOING".equalsIgnoreCase(appt.getStatus())) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            appt.setStatus("Completed");

            /* ================= 4. UPDATE STATUS ================= */
            String updateSql
                    = "UPDATE appointment SET status = ? WHERE appointment_id = ?";

            updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, appt.getStatus());
            updateStmt.setString(2, appt.getAppointmentId());

            int updated = updateStmt.executeUpdate();

            if (updated > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (checkStmt != null) {
                    checkStmt.close();
                }
            } catch (Exception ignored) {
            }
            try {
                if (updateStmt != null) {
                    updateStmt.close();
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
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
