package com.playops.app;

import com.playops.exceptions.FileProcessingException;
import com.playops.model.*;
import com.playops.repository.CustomerRepository;
import com.playops.repository.ProductRepository;
import com.playops.repository.TransactionRepository;
import com.playops.service.StoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ConsoleMain {

    public static void main(String[] args) throws Exception {
        StoreService store = new StoreService("PlayOps");
        try {
            store.initialize();
        } catch (Exception e) {
            System.out.println("Warning: initialize failed: " + e.getMessage());
        }

        Scanner sc = new Scanner(System.in);

        System.out.println("PlayOps - CLI");
        System.out.println("Type a number and press Enter.");

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("1) Show customers");
            System.out.println("2) Show inventory");
            System.out.println("3) Show transactions");
            System.out.println("4) Add sample records (constructors)");
            System.out.println("5) Add a customer (manual)");
            System.out.println("6) Add a console product (manual)");
            System.out.println("7) Add a digital game (manual)");
            System.out.println("8) Remove customer by email");
            System.out.println("9) Remove product by name");
            System.out.println("10) Save");
            System.out.println("0) Exit");
            System.out.print("> ");

            String input = sc.nextLine().trim();

            if (input.equals("1")) {
                List<Customer> customers = store.getAllCustomers();
                if (customers.isEmpty()) System.out.println("(no customers)");
                for (Customer c : customers) System.out.println(c);

            } else if (input.equals("2")) {
                List<Product> products = store.getAllProducts();
                if (products.isEmpty()) System.out.println("(no inventory)");
                for (Product p : products) System.out.println(p);

            } else if (input.equals("3")) {
                List<Transaction> tx = store.getAllTransactions();
                if (tx.isEmpty()) System.out.println("(no transactions)");
                for (Transaction t : tx) System.out.println(t);
                System.out.println("Total revenue: $" + store.getTotalRevenue());

            } else if (input.equals("4")) {
                // Manual constructor calls, nothing fancy
                Customer a = new Customer("Alice", "Johnson", "alice.johnson@example.com", "12 Maple Street");
                Customer b = new Customer("Bob", "Smith", "bob.smith@example.com", "8 Oak Avenue");

                Product ps5 = new Console(
                        "PlayStation 5",
                        "Disc edition console",
                        2020,
                        8,
                        499.99,
                        Console.Brand.SONY,
                        "PS5"
                );

                Product eldenRing = new DigitalGame(
                        "Elden Ring",
                        "Open-world action RPG",
                        2022,
                        40,
                        59.99,
                        Game.GameGenre.ACTION,
                        Game.GamePlatform.PC
                );

                store.addCustomer(a);
                store.addCustomer(b);
                store.addProduct(ps5);
                store.addProduct(eldenRing);

                store.saveNow();
                System.out.println("Added sample records and saved.");

            } else if (input.equals("5")) {
                System.out.print("First name: ");
                String first = sc.nextLine().trim();
                System.out.print("Last name: ");
                String last = sc.nextLine().trim();
                System.out.print("Email: ");
                String email = sc.nextLine().trim();
                System.out.print("Address: ");
                String address = sc.nextLine().trim();

                store.addCustomer(new Customer(first, last, email, address));
                store.saveNow();
                System.out.println("Customer added.");

            } else if (input.equals("6")) {
                System.out.print("Name: ");
                String name = sc.nextLine().trim();
                System.out.print("Description: ");
                String desc = sc.nextLine().trim();
                System.out.print("Year: ");
                int year = Integer.parseInt(sc.nextLine().trim());
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(sc.nextLine().trim());
                System.out.print("Price: ");
                double price = Double.parseDouble(sc.nextLine().trim());
                System.out.print("Brand (SONY/MICROSOFT/NINTENDO/VALVE): ");
                Console.Brand brand = Console.Brand.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Model: ");
                String model = sc.nextLine().trim();

                store.addProduct(new Console(name, desc, year, qty, price, brand, model));
                store.saveNow();
                System.out.println("Console product added.");

            } else if (input.equals("7")) {
                System.out.print("Name: ");
                String name = sc.nextLine().trim();
                System.out.print("Description: ");
                String desc = sc.nextLine().trim();
                System.out.print("Year: ");
                int year = Integer.parseInt(sc.nextLine().trim());
                System.out.print("Quantity: ");
                int qty = Integer.parseInt(sc.nextLine().trim());
                System.out.print("Price: ");
                double price = Double.parseDouble(sc.nextLine().trim());
                System.out.print("Genre (e.g. ACTION): ");
                Game.GameGenre genre = Game.GameGenre.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Platform (PC/MS_DOS/Xbox/PlayStation/NintendoSwitch/Nintendo64/GameBoy): ");
                Game.GamePlatform platform = Game.GamePlatform.valueOf(sc.nextLine().trim());

                store.addProduct(new DigitalGame(name, desc, year, qty, price, genre, platform));
                store.saveNow();
                System.out.println("Digital game added.");

            } else if (input.equals("8")) {
                System.out.print("Email to remove: ");
                String email = sc.nextLine().trim();
                removeCustomerByEmail(email);
                store.initialize(); // reload into service after external save
                System.out.println("Removed (if existed) and saved.");

            } else if (input.equals("9")) {
                System.out.print("Product name to remove: ");
                String name = sc.nextLine().trim();
                removeProductByName(name);
                store.initialize(); // reload into service after external save
                System.out.println("Removed (if existed) and saved.");

            } else if (input.equals("10")) {
                store.saveNow();
                System.out.println("Saved.");

            } else if (input.equals("0")) {
                running = false;

            } else {
                System.out.println("Unknown input.");
            }
        }

        try {
            store.saveNow();
        } catch (FileProcessingException e) {
            System.out.println("Save failed: " + e.getMessage());
        }

        System.out.println("Bye.");
        sc.close();
    }

    private static void removeCustomerByEmail(String email) throws FileProcessingException {
        CustomerRepository repo = new CustomerRepository();
        List<Customer> all = repo.load();
        List<Customer> kept = new ArrayList<>();
        for (Customer c : all) {
            if (!c.getEmail().equalsIgnoreCase(email)) {
                kept.add(c);
            }
        }
        repo.save(kept);
    }

    private static void removeProductByName(String name) throws FileProcessingException {
        ProductRepository repo = new ProductRepository();
        List<Product> all = repo.load();
        List<Product> kept = new ArrayList<>();
        for (Product p : all) {
            if (!p.getName().equalsIgnoreCase(name)) {
                kept.add(p);
            }
        }
        repo.save(kept);

        TransactionRepository txRepo = new TransactionRepository();
        txRepo.save(txRepo.load());
    }
}