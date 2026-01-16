package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Staff;
import db.DBConnection; 

public class StaffDaoImpl implements StaffDao {
    
    @Override
    public List<Staff> getAllStaff() {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff ORDER BY staff_id";
        
        try (Connection conn = DBConnection.getConnection(); // Uses your group's DBConnection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Staff staff = new Staff(
                    rs.getString("staff_id"),
                    rs.getString("name"),
                    rs.getString("nric"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password")
                );
                staffList.add(staff);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }
    
    @Override
    public Staff getStaffById(String staffId) {
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staffId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    staff = new Staff(
                            rs.getString("staff_id"),
                            rs.getString("name"),
                            rs.getString("nric"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    @Override
    public Staff getStaffByEmail(String email) {
        Staff staff = null;
        String sql = "SELECT * FROM staff WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    staff = new Staff(
                            rs.getString("staff_id"),
                            rs.getString("name"),
                            rs.getString("nric"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staff;
    }

    @Override
    public boolean addStaff(Staff staff) {
        String sql = "INSERT INTO staff (staff_id, name, nric, email, phone, password) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staff.getStaffId());
            pstmt.setString(2, staff.getName());
            pstmt.setString(3, staff.getNric());
            pstmt.setString(4, staff.getEmail());
            pstmt.setString(5, staff.getPhone());
            pstmt.setString(6, staff.getPassword());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateStaff(Staff staff) {
        String sql = "UPDATE staff SET name = ?, nric = ?, email = ?, phone = ?, password = ? WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staff.getName());
            pstmt.setString(2, staff.getNric());
            pstmt.setString(3, staff.getEmail());
            pstmt.setString(4, staff.getPhone());
            pstmt.setString(5, staff.getPassword());
            pstmt.setString(6, staff.getStaffId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteStaff(String staffId) {
        String sql = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staffId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM staff WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean checkNricExists(String nric) {
        String sql = "SELECT COUNT(*) FROM staff WHERE nric = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nric);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Staff> searchStaff(String keyword) {
        List<Staff> staffList = new ArrayList<>();
        String sql = "SELECT * FROM staff WHERE staff_id LIKE ? OR name LIKE ? OR email LIKE ? ORDER BY staff_id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Staff staff = new Staff(
                            rs.getString("staff_id"),
                            rs.getString("name"),
                            rs.getString("nric"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("password")
                    );
                    staffList.add(staff);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return staffList;
    }

    @Override
    public String generateNextStaffId() {
        String sql = "SELECT MAX(staff_id) FROM staff";
        String nextId = "STA001";

        try (Connection conn = DBConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String maxId = rs.getString(1);
                if (maxId != null) {
                    int num = Integer.parseInt(maxId.substring(3)) + 1;
                    nextId = String.format("STA%03d", num);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextId;
    }
}
