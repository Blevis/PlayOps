package com.playops.store;

import com.playops.model.Game;

import java.util.ArrayList;

public class Store {
    // Properties
    private String name;
    private ArrayList<Game> inventory = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();

    // Constructor
    public Store(String name) {
        this.name = name;
    }

    // Get/Set Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // com.playops.model.Game Related Methods
    public void addGame(Game game) {
        inventory.add(game);
        game.setQuantity(game.getQuantity() + 1);
        System.out.println(game.getTitle() + " has been added to the inventory.");
    }

    public void removeGame(String title) {
        Game game = findGame(title);
        if (game != null) {
            inventory.remove(game);
            System.out.println(title + " has been removed from the inventory.");
        } else {
            System.out.println(title + " not found in inventory.");
        }
    }

    public Game findGame(String title) {
        for (Game game : inventory) {
            if (game.getTitle().equalsIgnoreCase(title)) {
                return game;
            }
        }
        return null;
    }

    public void displayInventory() {
        System.out.println("Inventory: ");
        if (inventory.isEmpty()) {
            System.out.println(name + " has no inventory.");
            return;
        }
        System.out.printf("| %-3s | %-25s | %-8s | %-10s |%n",
                "ID", "Title", "Price/day", "Quantity");
        System.out.println("-------------------------------------------------------------");
        for (Game game : inventory) {
            System.out.printf("| %-3d | %-25s | $%-8.2f | %-10d |%n", game.getId(), game.getTitle(), game.getPricePerDay(), game.getQuantity());}
    }


    // com.playops.model.Customer Related Methods
    public void addCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("com.playops.model.Customer " + customer.getName() + " " + customer.getLastName() + " has been added.");
    }

    public Customer findCustomer(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    public void removeCustomer(String name) {
        Customer customer = findCustomer(name);
        if (customer != null) {
            customers.remove(customer);
            System.out.println("com.playops.model.Customer " + name  + " has been removed.");
        } else {
            System.out.println("com.playops.model.Customer " + name + " not found.");
        }
    }

    public void displayCustomers() {
        System.out.println("\ncom.playops.model.Customer List:");
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }

        System.out.printf("| %-3s | %-20s | %-20s | %-20s |%n", "ID", "Name", "Email", "Address");
        System.out.println("-----------------------------------------------------------------------------");
        for (Customer customer : customers) {
            String fullName = customer.getName() + " " + customer.getLastName();
            System.out.printf("| %-3d | %-20s | %-20s | %-20s |%n",
                    customer.getId(), fullName, customer.getEmail(), customer.getAddress());
        }
    }
}
