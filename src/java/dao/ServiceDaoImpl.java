package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.DBConnection;
import model.Service;

public class ServiceDaoImpl extends ServiceDao {

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM service";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Service s = new Service();
                s.setServiceID(rs.getInt("serviceID"));
                s.setServiceName(rs.getString("serviceName"));
                s.setServiceDesc(rs.getString("serviceDesc"));
                s.setServicePrice(rs.getDouble("servicePrice"));
                s.setServiceDuration(rs.getInt("serviceDuration"));
                services.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    public Service getServiceByID(int serviceID) {
        String sql = "SELECT * FROM service WHERE serviceID = ?";
        Service s = null;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, serviceID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                s = new Service();
                s.setServiceID(rs.getInt("serviceID"));
                s.setServiceName(rs.getString("serviceName"));
                s.setServiceDesc(rs.getString("serviceDesc"));
                s.setServicePrice(rs.getDouble("servicePrice"));
                s.setServiceDuration(rs.getInt("serviceDuration"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
}
