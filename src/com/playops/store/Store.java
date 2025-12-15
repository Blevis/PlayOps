package com.playops.store;

import com.playops.exceptions.*;
import com.playops.interfaces.PaymentMethod;
import com.playops.model.*;
import com.playops.payment.PaymentProcessor;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.io.PrintWriter;

import java.util.*;

public class Store {
    // Properties
    private static final String INVENTORY_FILE = "inventory.csv";
    private static final String CUSTOMER_FILE = "customers.csv";
    private static final String TRANSACTIONS_FILE = "transactions.csv";
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

    public ArrayList<Product> getInventory() {
        return inventory;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

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
            inventory.add(product);
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
        System.out.println("\nInventory list:");
        if (inventory.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-3s | %-8s | %-30s | %-6s | %-8s | %-10s | %-30s |%n",
                "ID", "Type", "Name", "Year", "Quantity", "Price", "Details");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
        for (Product p : inventory) {
            String type = getProductType(p);
            String details = getAdditionalInfo(p);
            System.out.printf("| %-3d | %-8s | %-30s | %-6d | %-8d | $%-9.2f | %-30s |%n",
                    p.getId(), type, p.getName(), p.getYear(), p.getQuantity(), p.getPrice(), details);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------------");
    }

    public void saveInventory() throws FileProcessingException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            writer.println("Id,Type,Name,Description,Year,Quantity,Price,Other infos");
            for (Product product : inventory) {
                writer.println(productToString(product));
            }
            System.out.println("Inventory file saved successfully.");
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save inventory file: " + e.getMessage());
        }
    }

    public void loadInventory() throws FileProcessingException {
        if (!Files.exists(Paths.get(INVENTORY_FILE))) {
            System.out.println("No inventory file found. Starting fresh.");
            return;
        }

        try {
            int loadedCount = 0;
            List<String> lines = Files.readAllLines(Paths.get(INVENTORY_FILE));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                Product product = parseProductLine(line);
                if (product != null) {
                    inventory.add(product);
                    loadedCount++;
                }
            }
            if (loadedCount > 0) {
                System.out.println("Inventory file loaded successfully. (" + loadedCount + " items)");
            } else {
                System.out.println("Inventory file is empty. Starting fresh.");
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load inventory file: " + e.getMessage());
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
        System.out.println("\nCustomers list:");
        if (customers.isEmpty()) {
            System.out.println("No customers registered.");
            return;
        }
        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.printf("| %-3s | %-20s | %-30s | %-30s |%n", "ID", "Name", "Email", "Address");
        System.out.println("------------------------------------------------------------------------------------------------");
        for (Customer customer : customers) {
            String fullName = customer.getName() + " " + customer.getLastName();
            System.out.printf("| %-3d | %-20s | %-30s | %-30s |%n",
                    customer.getId(), fullName, customer.getEmail(), customer.getAddress());
        }
        System.out.println("------------------------------------------------------------------------------------------------");
    }

    public void loadCustomers() throws FileProcessingException {
        if (!Files.exists(Paths.get(CUSTOMER_FILE))) {
            System.out.println("No customers file found. Starting fresh.");
            return;
        }

        try {
            int loadedCount = 0;
            List<String> lines = Files.readAllLines(Paths.get(CUSTOMER_FILE));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                Customer customer = parseCustomerLine(line);
                if (customer != null) {
                    customers.add(customer);
                    loadedCount++;
                }
            }
            if (loadedCount > 0) {
                System.out.println("Customers file loaded successfully. (" + loadedCount + " customers)");
            } else {
                System.out.println("Customers file is empty. Starting fresh.");
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load customers file: " + e.getMessage());
        }
    }

    public void saveCustomers() throws FileProcessingException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMER_FILE))) {
            writer.println("Id,Type,Name,Last Name,Email,Address");
            for (Customer customer : customers) {
                writer.println(customer.getId() + "," + customer.getName() + "," +
                        customer.getLastName() + "," + customer.getEmail() + "," +
                        customer.getAddress());
            }
            System.out.println("Customers file saved successfully.");
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save customers file: " + e.getMessage());
        }
    }

    /* |---------------------------------------- TRANSACTION -------------------------------------------------| */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void displayTransactions() {
        System.out.println("\nTransactions list:");
        if (transactions.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-6s | %-25s | %-30s | %-15s | %-19s |%n",
                "Type", "Customer Name", "Product Name", "Amount", "Time");
        System.out.println("---------------------------------------------------------------------------------------------------------------");
        for (Transaction t : transactions) {
            String customerName = t.getCustomer().getName() + " " + t.getCustomer().getLastName();
            String formattedTime = t.getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            System.out.printf("| %-6s | %-25s | %-30s | $%-14.2f | %-19s |%n",
                    t.getType(),
                    customerName,
                    t.getProduct().getName(),
                    t.getAmount(),
                    formattedTime);
        }
        System.out.println("---------------------------------------------------------------------------------------------------------------");
    }

    public double getTotalRevenue() {
        double total = 0;
        for (Transaction t : transactions) {
            total += t.getAmount();
        }
        return total;
    }

    public void saveTransactions() throws FileProcessingException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTIONS_FILE))) {
            writer.println("Type,Customer,Product,Price,Time");
            for (Transaction transaction : transactions) {
                Product product = transaction.getProduct();
                Customer customer = transaction.getCustomer();
                writer.println(transaction.getType() + "," +
                        customer.getName() + " " + customer.getLastName() + "," +
                        product.getName() + "," +
                        transaction.getAmount() + "," +
                        transaction.getTimestamp());
            }
            System.out.println("Transaction file saved successfully.");
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save transaction file: " + e.getMessage());
        }
    }

    public void loadTransactions() throws FileProcessingException {
        if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) {
            System.out.println("No transactions file found. Starting fresh.");
            return;
        }

        try {
            int loadedCount = 0;
            List<String> lines = Files.readAllLines(Paths.get(TRANSACTIONS_FILE));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                Transaction transaction = parseTransactionLine(line);
                if (transaction != null) {
                    transactions.add(transaction);
                    loadedCount++;
                }
            }
            if (loadedCount > 0) {
                System.out.println("Transaction file loaded successfully. (" + loadedCount + " transactions)");
            } else {
                System.out.println("Transaction file is empty. Starting fresh.");
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load transaction file: " + e.getMessage());
        }
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

    /* |---------------------------------------- HELPER METHODS -------------------------------------------------| */
    // Convert product to string format
    private String productToString(Product product) {
        if (product instanceof DigitalGame dg) {
            return String.format("%d,DIGITAL,%s,%s,%d,%d,%.1f,%s,%s",
                    dg.getId(), dg.getName(), dg.getDescription(), dg.getYear(),
                    dg.getQuantity(), dg.getPrice(), dg.getGenre(), dg.getPlatform());
        } else if (product instanceof PhysicalGame pg) {
            return String.format("%d,PHYSICAL,%s,%s,%d,%d,%.1f,%.2f,%s,%s",
                    pg.getId(), pg.getName(), pg.getDescription(), pg.getYear(),
                    pg.getQuantity(), pg.getPrice(), pg.getPricePerDay(),
                    pg.getGenre(), pg.getPlatform());
        } else if (product instanceof Console c) {
            return String.format("%d,CONSOLE,%s,%s,%d,%d,%.1f,%s,%s",
                    c.getId(), c.getName(), c.getDescription(), c.getYear(),
                    c.getQuantity(), c.getPrice(), c.getBrand(), c.getModel());
        } else if (product instanceof Desktop d) {
            return String.format("%d,DESKTOP,%s,%s,%d,%d,%.1f,%s,%s",
                    d.getId(), d.getName(), d.getDescription(), d.getYear(),
                    d.getQuantity(), d.getPrice(), d.getBrand(), d.getModel());
        } else if (product instanceof Laptop l) {
            return String.format("%d,LAPTOP,%s,%s,%d,%d,%.1f,%s,%s,%.2f",
                    l.getId(), l.getName(), l.getDescription(), l.getYear(),
                    l.getQuantity(), l.getPrice(), l.getBrand(), l.getModel(), l.getScreenSize());
        }
        return "";
    }

    // Parse a line into a Product
    private Product parseProductLine(String line) throws FileProcessingException {
        String[] parts = line.split(",");
        if (parts.length < 7) return null;

        try {
            String type = parts[1];
            String name = parts[2];
            String description = parts[3];
            int year = Integer.parseInt(parts[4]);
            int quantity = Integer.parseInt(parts[5]);
            double price = Double.parseDouble(parts[6]);

            switch (type) {
                case "DIGITAL":
                    Game.GameGenre genre = Game.GameGenre.valueOf(parts[7]);
                    Game.GamePlatform platform = Game.GamePlatform.valueOf(parts[8]);
                    return new DigitalGame(name, description, year, quantity, price, genre, platform);

                case "PHYSICAL":
                    double pricePerDay = Double.parseDouble(parts[7]);
                    genre = Game.GameGenre.valueOf(parts[8]);
                    platform = Game.GamePlatform.valueOf(parts[9]);
                    return new PhysicalGame(name, description, year, quantity, price, pricePerDay, genre, platform);

                case "CONSOLE":
                    Console.Brand brand = Console.Brand.valueOf(parts[7]);
                    String model = parts[8];
                    return new Console(name, description, year, quantity, price, brand, model);

                case "DESKTOP":
                    Desktop.Brand desktopBrand = Desktop.Brand.valueOf(parts[7]);
                    model = parts[8];
                    return new Desktop(name, description, year, quantity, price, desktopBrand, model);

                case "LAPTOP":
                    Laptop.Brand laptopBrand = Laptop.Brand.valueOf(parts[7]);
                    model = parts[8];
                    double screenSize = Double.parseDouble(parts[9]);
                    return new Laptop(name, description, year, quantity, price, laptopBrand, model, screenSize);
            }
        } catch (Exception e) {
            throw new FileProcessingException("Error parsing product line: " + e.getMessage());
        }
        return null;
    }

    // Parse a line into Customer
    private Customer parseCustomerLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null;

        try {
            String name = parts[1];
            String lastName = parts[2];
            String email = parts[3];
            String address = parts[4];
            return new Customer(name, lastName, email, address);
        } catch (Exception e) {
            System.out.println("Error parsing customer line: " + e.getMessage());
            return null;
        }
    }

    // Parse a line into Transaction
    private Transaction parseTransactionLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null;

        try {
            Transaction.Type type = Transaction.Type.valueOf(parts[0].trim());
            String customerName = parts[1].trim();
            String productName = parts[2].trim();
            double amount = Double.parseDouble(parts[3].trim());

            Product product = findProductByName(productName);
            Customer customer = findCustomerByName(customerName);

            return new Transaction(product, customer, type, amount);
        } catch (Exception e) {
            System.out.println("Error parsing transaction line: " + e.getMessage());
            return null;
        }
    }

    // Get product type
    private String getProductType(Product product) {
        if (product instanceof DigitalGame) return "DIGITAL";
        if (product instanceof PhysicalGame) return "PHYSICAL";
        if (product instanceof Console) return "CONSOLE";
        if (product instanceof Desktop) return "DESKTOP";
        if (product instanceof Laptop) return "LAPTOP";
        return "UNKNOWN";
    }

    // Get additional info from a product type
    private String getAdditionalInfo(Product product) {
        if (product instanceof DigitalGame dg) {
            return dg.getGenre() + ", " + dg.getPlatform();
        } else if (product instanceof PhysicalGame pg) {
            return pg.getGenre() + ", " + pg.getPlatform() + ", $" + pg.getPricePerDay() + "/day";
        } else if (product instanceof Console c) {
            return c.getBrand() + " " + c.getModel();
        } else if (product instanceof Desktop d) {
            return d.getBrand() + " " + d.getModel();
        } else if (product instanceof Laptop l) {
            return l.getBrand() + " " + l.getModel() + ", " + l.getScreenSize() + "\"";
        }
        return "";
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
