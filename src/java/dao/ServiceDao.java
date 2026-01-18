package dao;

import db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDao {

    public int getTotalServices() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM APP.SERVICES";

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
}
