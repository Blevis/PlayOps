package com.playops.store;

import com.playops.exceptions.*;
import com.playops.interfaces.PaymentMethod;
import com.playops.model.*;
import com.playops.payment.PaymentProcessor;

import java.util.ArrayList;

public class Store {
    // Properties
    private String name;
    private ArrayList<Product> inventory = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private PaymentProcessor paymentProcessor = new PaymentProcessor();

    // Constructor
    public Store(String name) {
        setName(name);
    }

    // Getters
    public String getName() {return name;}

    // Setters
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Store name cannot be blank");
        this.name = name;
    }

    /* |---------------------------------------- INVENTORY -------------------------------------------------| */
    public void addProduct(Product product){
        boolean found = false;
        for (Product p : inventory) {
            if (p.getId() == product.getId()) {
                p.increaseQuantity(1);
                found = true;
                break;
            }
        }
        if (!found) {
            inventory.add(product);   // Add new product to inventory
        }
        System.out.println(product.getName() + " added to inventory.");
    }

    public void removeProduct(int id) throws ItemNotFoundException {
        Product product = findProductById(id);

        if (product.getQuantity() > 1) {
            product.decreaseQuantity(1);
            System.out.println("One unit of " + product.getName() + " removed. Remaining quantity: " + product.getQuantity());
        } else {
            inventory.remove(product);
            System.out.println(product.getName() + " completely removed from inventory.");
        }
    }
    public Product findProductById(int id) throws ItemNotFoundException {
        for (Product p : inventory) {
            if (p.getId() == id)
                return p;
        }
        throw new ItemNotFoundException("Product with ID " + id + " not found");
    }

    public Product findProductByName(String name) throws ItemNotFoundException {
        for (Product p : inventory) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        throw new ItemNotFoundException("Product with name \"" + name + "\" not found.");
    }

    public void displayInventory(){
        System.out.println("\nInventory");
        if (inventory.isEmpty()) {
            System.out.println("No products available");
            return;
        }
        for (Product p : inventory) {
            System.out.println(p);
        }
    }

    /* |---------------------------------------- CUSTOMER -------------------------------------------------| */
    public void addCustomer(Customer customer) {
        customers.add(customer);
        System.out.println("Customer " + customer.getName() + " " + customer.getLastName() + " has been added.");
    }
    public void removeCustomer(int id) throws ItemNotFoundException {
        Customer c = findCustomerById(id);
        customers.remove(c);
        System.out.println("Customer " + c.getName() + " " + c.getLastName() + " has been removed.");
    }
    public Customer findCustomerById(int id) throws ItemNotFoundException {
        for (Customer c : customers) {
            if (c.getId() == id) return c;
        }
        throw new ItemNotFoundException("Customer with ID " + id + " not found.");
    }
    public Customer findCustomerByName(String fullName) throws ItemNotFoundException {
        for (Customer c : customers) {
            String customerFullName = c.getName() + " " + c.getLastName();
            if (customerFullName.equalsIgnoreCase(fullName)) {
                return c;
            }
        }
        throw new ItemNotFoundException("Customer \"" + fullName + "\" not found.");
    }
    public void displayCustomers() {
        System.out.println("\nCustomer list:");
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

    /* |---------------------------------------- TRANSACTION -------------------------------------------------| */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void displayTransactions() {
        System.out.println("\nTransactions:");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Transaction t : transactions) {
            total += t.getAmount();
        }
        return total;
    }

    /* |---------------------------------------- PURCHASE / RENT -------------------------------------------------| */
    public void buyProduct(int id, Customer customer, PaymentMethod method) throws ItemNotFoundException, PaymentException{
        try {
            Product product = findProductById(id);
            if (!product.isAvailable())
                throw new ItemNotFoundException("Product out of stock");

            double price = product.getPrice();

            paymentProcessor.process(method, price);
            product.decreaseQuantity(1);

            Transaction t = new Transaction(product, customer, Transaction.Type.BUY, price);
            addTransaction(t);

            System.out.println("Purchase successful.");

        } catch(PaymentException e){
            System.out.println("Payment failed: "+e.getMessage());
        } catch(Exception e){
            System.out.println("Transaction error: " + e.getMessage());
        }
    }

    public void rentGame(int id, Customer customer, PaymentMethod method, int days) throws ItemNotFoundException, FileProcessingException, PaymentException {
        try {
            Product product = findProductById(id);
            if (!(product instanceof Game game)) {
                throw new FileProcessingException("This item is not rentable");
            }
            if (!game.isAvailable())
                throw new ItemNotFoundException("Game out of stock");

            double rentalPrice = game.calculateRentalPrice(days);

            paymentProcessor.process(method, rentalPrice);
            game.decreaseQuantity(1);

            Transaction t = new Transaction(game, customer, Transaction.Type.RENT, rentalPrice);
            addTransaction(t);

            System.out.println("Rental successful for " + days + " day(s).");

        } catch (PaymentException e) {
            System.out.println("Payment failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Transaction error: " + e.getMessage());
        }
    }

    // To string
    @Override
    public String toString() {
        return "Store: " + name +
                "\nProducts in inventory: " + inventory.size() +
                "\nCustomers registered: " + customers.size() +
                "\nTransactions recorded: " + transactions.size() +
                "\nTotal revenue: $" + getTotalRevenue();
    }
}
