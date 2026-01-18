package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentDao {

    public int getTotalAppointments() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM APP.APPOINTMENT";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total;
    }

    //get recent appointments
    public java.util.List<String[]> getRecentAppointments(int limit) {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT appointment_id, customer_id, service_id, appointment_date, status "
                + "FROM APP.APPOINTMENT "
                + "ORDER BY appointment_date DESC "
                + "FETCH FIRST ? ROWS ONLY";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] row = new String[5];
                row[0] = String.valueOf(rs.getInt("appointment_id"));
                row[1] = String.valueOf(rs.getInt("customer_id"));
                row[2] = String.valueOf(rs.getInt("service_id"));
                row[3] = String.valueOf(rs.getDate("appointment_date"));
                row[4] = rs.getString("status");
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
