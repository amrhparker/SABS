package model;
import java.sql.Timestamp;

public class Appointment implements java.io.Serializable{

    private String appointmentId;
    private Timestamp appointmentDatetime;
    private String status;
    private String customerId;
    private String serviceId;

    public Appointment() {
    }

    public Appointment(String appointmentId, Timestamp appointmentDatetime,
            String status, String customerId, String serviceId) {
        this.appointmentId = appointmentId;
        this.appointmentDatetime = appointmentDatetime;
        this.status = status;
        this.customerId = customerId;
        this.serviceId = serviceId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }


    public void setAppointmentDatetime(Timestamp appointmentDatetime) {
        this.appointmentDatetime = appointmentDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public Timestamp getAppointmentDatetime() {
        return appointmentDatetime;
    }
}
