package carsharing.ui;

import carsharing.domain.Car;
import carsharing.domain.Company;
import carsharing.repository.Dao;

import java.util.List;
import java.util.Scanner;

public class ManagerUI {
    private static Dao dao;
    private static final Scanner input = new Scanner(System.in);
    private static final Scanner localInput = new Scanner(System.in);

    public ManagerUI(Dao dao) {
        ManagerUI.dao = dao;
        localInput.useDelimiter(System.lineSeparator());
    }

    public void menu() {
        int option = 1;
        while (option != 0) {
            System.out.println("1. Company list");
            System.out.println("2. Create a company");
            System.out.println("0. Back");

            option = input.nextInt();
            System.out.println();

            switch (option) {
                case 1 -> listCompanies();
                case 2 -> createCompany();
            }
        }
    }

    public void createCompany() {
        System.out.println("Enter the company name:");
        String name = localInput.nextLine();

        Boolean wasAdded = dao.createCompany(name);
        if (wasAdded) {
            System.out.println("The company was created!");
        } else {
            System.out.println("The company was not created.");
        }
        System.out.println();
    }

    public void companyMenu(Integer id) {
        int option = 1;

        while (option != 0) {
            System.out.println("'" + dao.getCompanyNameFromId(id) + "' company:");
            System.out.println("1. Car list");
            System.out.println("2. Create a car");
            System.out.println("0. Back");
            option = input.nextInt();
            System.out.println();

            switch (option) {
                case 1 -> listCompanyCars(id);
                case 2 -> createCompanyCar(id);
            }
        }
    }

    public void createCompanyCar(Integer id) {
        System.out.println("Enter the car name:");
        String name = localInput.nextLine();

        Boolean wasAdded = dao.createCar(name, id);
        if (wasAdded) {
            System.out.println("The car was added!");
        } else {
            System.out.println("The car was not added.");
        }
        System.out.println();
    }

    public void listCompanies() {
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
                int id = dao.getCompanyIdFromName(companies.get(choice - 1).getName());
                if (companies.stream().anyMatch(x -> x.getId() == id)) {
                    companyMenu(id);
                }
            }
        }
    }

    public void listCompanyCars(Integer id) {
        List<Car> cars = dao.getCompanyCars(id);

        if (cars.size() == 0) {
            System.out.println("The car list is empty!");
            System.out.println();
        } else {
            System.out.println("Car list:");
            for (int i = 0; i < cars.size(); i++) {
                System.out.println((i + 1) + ". " + cars.get(i).getName());
            }
            System.out.println();
        }
    }
}
