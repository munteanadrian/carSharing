package carsharing.ui;

import carsharing.domain.Car;
import carsharing.domain.Company;
import carsharing.domain.Customer;
import carsharing.repository.Dao;

import java.util.List;
import java.util.Scanner;

public class CustomerUI {

    private static Dao dao;
    private static final Scanner input = new Scanner(System.in);
    private static final Scanner localInput = new Scanner(System.in);

    public CustomerUI(Dao dao) {
        CustomerUI.dao = dao;

        localInput.useDelimiter(System.lineSeparator());
    }

    public void createCustomer() {
        System.out.println("Enter the customer name:");
        String name = localInput.nextLine();

        Boolean wasAdded = dao.createCustomer(name);
        if (wasAdded) {
            System.out.println("The customer was added!");
        } else {
            System.out.println("The customer was not added.");
        }
        System.out.println();
    }

    public void returnCar(int id) {
        Boolean status = dao.checkRentStatus(id);

        System.out.println();
        if (!status) {
            System.out.println("You didn't rent a car!");
        } else {
            dao.returnCar(id);
            System.out.println("You've returned a rented car!");
        }
        System.out.println();
    }

    public void listCustomers() {
        List<Customer> customers;
        customers = dao.getCustomers();

        if (customers.size() == 0) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("The customer list:");
            for (int i = 0; i < customers.size(); i++) {
                System.out.println((i + 1) + ". " + customers.get(i).getName());
            }
            System.out.println("0. Back");

            int choice = input.nextInt();
            System.out.println();

            if (choice > 0 && choice < customers.size() + 1) {
                int customerId = dao.getCustomerIdFromName(customers.get(choice - 1).getName());
                if (customers.stream().anyMatch(x -> x.getId() == customerId)) {
                    menu(customerId);
                }
            }
        }
        System.out.println();
    }

    public void menu(int customerId) {
        int option = 1;

        while (option != 0) {
            System.out.println("1. Rent a car");
            System.out.println("2. Return a rented car");
            System.out.println("3. My rented car");
            System.out.println("0. Back");

            option = input.nextInt();

            switch (option) {
                case 1 -> rentCar(customerId);
                case 2 -> returnCar(customerId);
                case 3 -> myRentedCar(customerId);
            }
        }
    }

    public void myRentedCar(int customerId) {
        Boolean status = dao.checkRentStatus(customerId);

        System.out.println();
        if (!status) {
            System.out.println("You didn't rent a car!");
        } else {
            String car = dao.getCarOfCustomer(customerId);
            String company = dao.getCompanyOfCar(car);

            System.out.println("Your rented car:");
            System.out.println(car);
            System.out.println("Company:");
            System.out.println(company);
        }
        System.out.println();
    }

    public void rentCar(int customerId) {
        Boolean status = dao.checkRentStatus(customerId);

        System.out.println();
        if (status) {
            System.out.println("You've already rented a car!");
        } else {
            listCompanies(customerId);
        }
        System.out.println();
    }

    public void listCompanies(int customerId) {
        List<Company> companies = dao.getCompanies();

        if (companies.size() == 0) {
            System.out.println("The company list is empty!");
            System.out.println();
        } else {
            System.out.println("Choose the company:");
            for (int i = 0; i < companies.size(); i++) {
                System.out.println((i + 1) + ". " + companies.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = input.nextInt();
            System.out.println();

            if (choice > 0 && choice < companies.size() + 1) {
                int companyId = dao.getCompanyIdFromName(companies.get(choice - 1).getName());
                if (companies.stream().anyMatch(x -> x.getId() == companyId)) {
                    listCompanyCars(companyId, customerId);
                }
            }
        }
    }

    public void listCompanyCars(int companyId, int customerId) {
        List<Car> cars = dao.getCompanyCarsNotRented(companyId);

        if (cars.size() == 0) {
            String name = dao.getCompanyNameFromId(companyId);
            System.out.println("No available cars in the '" + name + "' company");
        } else {
            System.out.println("Choose a car:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
            System.out.println("0. Back");
            int choice = input.nextInt();
            System.out.println();

            if (choice > 0 && choice < cars.size() + 1) {
                int carId = dao.getCompanyCarIdFromName(cars.get(choice - 1).getName());
                if (cars.stream().anyMatch(x -> x.getId() == carId)) {
                    boolean successful = dao.rentCar(customerId, carId);
                    if (successful) {
                        System.out.println("You rented '" + cars.get(choice - 1).getName() + "'");
                    }
                }
            }
        }
    }
}
