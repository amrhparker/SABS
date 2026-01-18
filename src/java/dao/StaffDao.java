package dao;

import java.util.List;
import model.Staff;

public interface StaffDao {
    List<Staff> getAllStaff();
    Staff getStaffById(String staffId);
    Staff getStaffByEmail(String email);
    boolean addStaff(Staff staff);
    boolean updateStaff(Staff staff);
    boolean deleteStaff(String staffId);
    boolean checkEmailExists(String email);
    boolean checkNricExists(String nric);
    List<Staff> searchStaff(String keyword);
    String generateNextStaffId();
    int getTotalStaffs();
}