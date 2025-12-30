package dao;

import java.util.List;
import model.Service;

public interface ServiceDao {

    List<Service> getAllServices();

    Service getServiceByID(int serviceID);
}
