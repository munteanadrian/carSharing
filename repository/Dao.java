package carsharing.repository;

import carsharing.domain.Car;
import carsharing.domain.Company;
import carsharing.domain.Customer;

import java.util.List;

public interface Dao {
    // MANAGER
    Boolean createCompany(String name);
    Boolean createCar(String name, Integer companyID);

    // CUSTOMER
    List<Customer> getCustomers();
    Boolean createCustomer(String name);
    Boolean checkRentStatus(int id);
    String getCarOfCustomer(int id);
    String getCompanyOfCar(String car);
    int getCompanyCarIdFromName(String car);

    boolean rentCar(int customerId, int carId);

    void returnCar(int id);


    // UTILS
    Integer getCompanyIdFromName(String name);
    Integer getCustomerIdFromName(String name);
    String getCompanyNameFromId(Integer id);
    Integer getLastIDForCarGroup(int companyId);
    List<Company> getCompanies();
    List<Car> getCompanyCars(Integer id);
    List<Car> getCompanyCarsNotRented(Integer id);
    void close();
}
