package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    // Get recent appointments with service names
    public List<String[]> getRecentAppointments(int limit) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT a.appointment_id, a.customer_id, "
                + "a.appointment_datetime, a.status, s.service_name "
                + "FROM APP.APPOINTMENT a "
                + "LEFT JOIN APP.SERVICES s ON a.service_id = s.service_id "
                + "ORDER BY a.appointment_datetime DESC "
                + "FETCH FIRST " + limit + " ROWS ONLY";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String[] row = new String[5];
                row[0] = rs.getString("appointment_id");
                row[1] = String.valueOf(rs.getInt("customer_id"));
                // Get timestamp and format it
                java.sql.Timestamp timestamp = rs.getTimestamp("appointment_datetime");
                if (timestamp != null) {
                    SimpleDateFormat sdf
                            = new SimpleDateFormat("dd MMM yyyy | h:mm a");
                    row[2] = sdf.format(timestamp);
                } else {
                    row[2] = "";
                }
                row[3] = rs.getString("service_name");
                row[4] = rs.getString("status");
                list.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
