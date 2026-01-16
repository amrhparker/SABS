package model;

public class Staff {
    private String staffId;
    private String name;
    private String nric;
    private String email;
    private String phone;
    private String password;
    
    public Staff() {}
    
    public Staff(String staffId, String name, String nric, String email, String phone, String password) {
        this.staffId = staffId;
        this.name = name;
        this.nric = nric;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }
    
    public String getStaffId() { return staffId; }
    public void setStaffId(String staffId) { this.staffId = staffId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getNric() { return nric; }
    public void setNric(String nric) { this.nric = nric; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}