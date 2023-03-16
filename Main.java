package carsharing;

import carsharing.repository.Dao;
import carsharing.repository.DaoImpl;
import carsharing.ui.CustomerUI;
import carsharing.ui.ManagerUI;

import java.util.Scanner;

public class Main {
    private static Dao dao;
    static final Scanner input = new Scanner(System.in);
    static final Scanner localInput = new Scanner(System.in);
    private final ManagerUI manager;
    private final CustomerUI customer;

    public Main() {
        dao = new DaoImpl();
        localInput.useDelimiter(System.lineSeparator());

        manager = new ManagerUI(dao);
        customer = new CustomerUI(dao);

        start();
    }

    public static void main(String[] args) {
        Main companiesDb = new Main();
    }

    public void start() {
        int option = 1;

        while (option != 0) {
            System.out.println("1. Log in as a manager");
            System.out.println("2. Log in as a customer");
            System.out.println("3. Create a customer");
            System.out.println("0. Exit");

            option = input.nextInt();
            System.out.println();

            switch (option) {
                case 1 -> manager.menu();
                case 2 -> customer.listCustomers();
                case 3 -> customer.createCustomer();
                case 0 -> dao.close();
            }
        }
    }

}