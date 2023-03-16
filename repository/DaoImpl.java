package carsharing.repository;

import carsharing.domain.Car;
import carsharing.domain.Company;
import carsharing.domain.Customer;
import org.h2.util.json.JSONValidationTargetWithoutUniqueKeys;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoImpl implements Dao {
    private static Connection connection;
    private static Statement statement;

    public DaoImpl() {
        DaoImpl.connect();
        DaoImpl.createTables();
    }

    public static void connect() {
        try {
            final String JDBC_DRIVER = "org.h2.Driver";
            final String DB_URL = "jdbc:h2:file:G:\\My Drive\\FSEGA An 3\\oop\\Car Sharing\\Car Sharing\\task\\src\\carsharing\\db\\carsharing;DB_CLOSE_ON_EXIT=TRUE";

            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
            statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createTables() {
        try {
            if (connection != null) {
                String createCompanyTableSQL = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                        "    ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "    NAME VARCHAR(50) NOT NULL UNIQUE);";
                statement.executeUpdate(createCompanyTableSQL);

                String createCarTableSQL = "CREATE TABLE IF NOT EXISTS CAR (" +
                        "  ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "  NAME VARCHAR(50) UNIQUE NOT NULL," +
                        "  COMPANY_ID INT NOT NULL," +
                        "  RENTED BIT NOT NULL DEFAULT 0," +
                        "  FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID));";
                statement.executeUpdate(createCarTableSQL);

                String createCustomerTableSQL = "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                        "  ID INT PRIMARY KEY AUTO_INCREMENT," +
                        "  NAME VARCHAR(50) UNIQUE NOT NULL," +
                        "  RENTED_CAR_ID INT," +
                        "  FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID));";
                statement.executeUpdate(createCustomerTableSQL);

//                String resetCompanySQL = "ALTER TABLE COMPANY " +
//                        "ALTER COLUMN ID RESTART WITH 1;";
//                statement.executeUpdate(resetCompanySQL);
//
//                String resetCarSQL = "ALTER TABLE CAR " +
//                        "ALTER COLUMN ID RESTART WITH 1;";
//                statement.executeUpdate(resetCarSQL);
//
//                String resetCustomerSQL = "ALTER TABLE CUSTOMER " +
//                        "ALTER COLUMN ID RESTART WITH 1;";
//                statement.executeUpdate(resetCustomerSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // MANAGER

    public Boolean createCompany(String name) {
        Boolean added = Boolean.FALSE;

        try {
            String newCompanySQL = "INSERT INTO COMPANY (NAME) VALUES ('" + name + "');";
            statement.executeUpdate(newCompanySQL);
            added = Boolean.TRUE;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return added;
    }

    public Boolean createCar(String name, Integer companyID) {
        Boolean wasAdded = Boolean.FALSE;

        try {
            String addCarSQL = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + name + "', " + companyID + ")";
            statement.executeUpdate(addCarSQL);
            wasAdded = Boolean.TRUE;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return wasAdded;
    }

    // CUSTOMER

    public List<Customer> getCustomers() {
        List<Customer> customers = new ArrayList<>();

        try {
            String getCustomersSQL = "SELECT * FROM CUSTOMER ORDER BY ID";
            ResultSet res = statement.executeQuery(getCustomersSQL);
            while (res.next()) {
                int id = res.getInt("ID");
                String name = res.getString("NAME");
                int rented_car_id = res.getInt("RENTED_CAR_ID");
                customers.add(new Customer(id, name, rented_car_id));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customers;
    }

    public Boolean createCustomer(String name) {
        boolean wasAddded = false;

        try {
            String createCustomerSQL = "INSERT INTO CUSTOMER (NAME) VALUES ('" + name + "');";
            statement.executeUpdate(createCustomerSQL);
            wasAddded = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return wasAddded;
    }

    @Override
    public Boolean checkRentStatus(int id) {
        boolean rent = false;

        try {
            String checkRentSQL = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = " + id + ";";
            ResultSet res = statement.executeQuery(checkRentSQL);
            res.next();
            String result = res.getString("RENTED_CAR_ID");
            if (result != null) {
                rent = !result.equals("null");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rent;
    }

    @Override
    public String getCarOfCustomer(int id) {
        String car = "";

        try {
            String getRentedCarIdSQL = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = " + id;
            ResultSet res = statement.executeQuery(getRentedCarIdSQL);
            res.next();
            int rentedCarId = res.getInt("RENTED_CAR_ID");

            String getCarFromIdSQL = "SELECT NAME FROM CAR WHERE ID = " + rentedCarId + ";";
            ResultSet res2 = statement.executeQuery(getCarFromIdSQL);
            res2.next();
            car = res2.getString("NAME");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return car;
    }

    @Override
    public String getCompanyOfCar(String car) {
        String company = "";

        try {
            String getCompanyIDFromCarNameSQL = "SELECT COMPANY_ID FROM CAR WHERE NAME = '" + car +"';";
            ResultSet res = statement.executeQuery(getCompanyIDFromCarNameSQL);
            res.next();
            int companyId = res.getInt("COMPANY_ID");

            String getCompanyNameFromCarId = "SELECT NAME FROM COMPANY WHERE ID = " + companyId + ";";
            ResultSet res2 = statement.executeQuery(getCompanyNameFromCarId);
            res2.next();
            company = res2.getString("NAME");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return company;
    }

    @Override
    public int getCompanyCarIdFromName(String car) {
        int id = -1;

        try {
            String getCompanyCarIdFromNameSQL = "SELECT ID FROM CAR WHERE NAME = '" + car + "';";
            ResultSet res = statement.executeQuery(getCompanyCarIdFromNameSQL);
            res.next();
            id = res.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    @Override
    public boolean rentCar(int customerId, int carId) {
        boolean successful = false;
        try {
            String rentCarSQL = "UPDATE CUSTOMER SET RENTED_CAR_ID = " + carId + " WHERE ID = " + customerId + ";";
            statement.executeUpdate(rentCarSQL);
            String setRentedInCarToTrue = "UPDATE CAR SET RENTED = 1 WHERE ID = " + carId + ";";
            statement.executeUpdate(setRentedInCarToTrue);

            successful = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return successful;
    }

    @Override
    public void returnCar(int id) {
        try {
            String getCarIdFromCustomer = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID = " + id + ";";
            ResultSet carIdRes = statement.executeQuery(getCarIdFromCustomer);
            carIdRes.next();
            int carId = carIdRes.getInt("RENTED_CAR_ID");

            String returnCarSQL = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = " + id + ";";
            statement.executeUpdate(returnCarSQL);

            String setRentedToFalse = "UPDATE CAR SET RENTED = 0 WHERE ID = " + carId + ";";
            statement.executeUpdate(setRentedToFalse);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // UTILS

    public Integer getCompanyIdFromName(String name) {
        int id = -1;

        try {
            String getCompanyNameSQL = "SELECT ID FROM COMPANY WHERE NAME = '" + name + "'";
            ResultSet res = statement.executeQuery(getCompanyNameSQL);
            res.next();
            id = res.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    public Integer getCustomerIdFromName(String name) {
        int id = -1;

        try {
            String getCompanyNameSQL = "SELECT ID FROM CUSTOMER WHERE NAME = '" + name + "'";
            ResultSet res = statement.executeQuery(getCompanyNameSQL);
            res.next();
            id = res.getInt("ID");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    public String getCompanyNameFromId(Integer id) {
        String name = "";

        try {
            String getCompanyNameSQL = "SELECT NAME FROM COMPANY WHERE ID = " + id;
            ResultSet res = statement.executeQuery(getCompanyNameSQL);
            res.next();
            name = res.getString("NAME");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return name;
    }

    public Integer getLastIDForCarGroup(int companyId) {
        int maxID = 1;

        try {
            String getLastIDSQL = "SELECT MAX(ID) AS FOUND FROM CAR WHERE COMPANY_ID " + companyId + ";";
            ResultSet result = statement.executeQuery(getLastIDSQL);
            result.next();
            maxID = result.getInt("FOUND");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (NullPointerException ignored) {

        }

        return maxID;
    }

    public List<Company> getCompanies() {
        List<Company> companyList = new ArrayList<>();

        try {
            String sql = "SELECT * FROM COMPANY ORDER BY ID;";
            ResultSet results = statement.executeQuery(sql);

            while (results.next()) {
                Integer id = results.getInt("ID");
                String name = results.getString("NAME");
                Company company = new Company(id, name);
                companyList.add(company);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return companyList;
    }

    public List<Car> getCompanyCars(Integer id) {
        List<Car> cars = new ArrayList<>();

        try {
            String getCarListSQL = "SELECT * FROM CAR WHERE COMPANY_ID = " + id;
            ResultSet results = statement.executeQuery(getCarListSQL);
            while (results.next()) {
                Integer carId = results.getInt("ID");
                String name = results.getString("NAME");
                cars.add(new Car(carId, name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cars;
    }

    @Override
    public List<Car> getCompanyCarsNotRented(Integer id) {
        List<Car> cars = new ArrayList<>();

        try {
            String getCarListSQL = "SELECT * FROM CAR WHERE COMPANY_ID = " + id + " AND RENTED = 0";
            ResultSet results = statement.executeQuery(getCarListSQL);
            while (results.next()) {
                Integer carId = results.getInt("ID");
                String name = results.getString("NAME");
                cars.add(new Car(carId, name));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cars;
    }

    public void close() {
        try {
            connection.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
