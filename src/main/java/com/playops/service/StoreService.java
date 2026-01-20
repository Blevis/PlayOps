package com.playops.service;

import com.playops.model.*;
import com.playops.repository.*;
import com.playops.exceptions.*;
import com.playops.interfaces.PaymentMethod;
import com.playops.payment.PaymentProcessor;
import java.util.List;
import java.util.Scanner;

public class StoreService {
    private ProductRepository productRepository;
    private CustomerRepository customerRepository;
    private TransactionRepository transactionRepository;
    private PaymentProcessor paymentProcessor;

    public StoreService(String storeName) {
        this.productRepository = new ProductRepository();
        this.customerRepository = new CustomerRepository();
        this.transactionRepository = new TransactionRepository();
        this.paymentProcessor = new PaymentProcessor();
        this.transactionRepository.setStoreService(this);
    }

    public void initialize() throws FileProcessingException {
        productRepository.load();
        customerRepository.load();
        transactionRepository.load();
    }

    public void shutdown() throws FileProcessingException {
        productRepository.save(productRepository.findAll());
        customerRepository.save(customerRepository.findAll());
        transactionRepository.save(transactionRepository.findAll());
    }

    public void saveNow() throws FileProcessingException {
        shutdown();
    }

    public void addCustomer(Customer customer) throws FileProcessingException {
        if (customer == null) throw new IllegalArgumentException("Customer cannot be null");
        var list = customerRepository.findAll();
        list.add(customer);
        customerRepository.save(list);
    }

    public void addProduct(Product product) throws FileProcessingException {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        var list = productRepository.findAll();
        list.add(product);
        productRepository.save(list);
    }

    public void restockProductByName(String name, int amount) throws ItemNotFoundException, FileProcessingException {
        Product p = findProductByName(name);
        p.increaseQuantity(amount);
        productRepository.save(productRepository.findAll());
    }

    public void buyProductByName(String productName, Customer customer, PaymentMethod paymentMethod)
            throws ItemNotFoundException, PaymentException {
        Product product = findProductByName(productName);
        if (!product.isAvailable()) {
            throw new ItemNotFoundException("Product out of stock");
        }

        double price = product.getPrice();
        paymentProcessor.process(paymentMethod, price);
        product.decreaseQuantity(1);

        Transaction transaction = new Transaction(product, customer, Transaction.Type.BUY, price);
        transactionRepository.add(transaction);
    }

    public void rentGameByName(String productName, Customer customer, PaymentMethod paymentMethod, int days)
            throws ItemNotFoundException, PaymentException, FileProcessingException {
        Product product = findProductByName(productName);
        if (!(product instanceof Game game)) {
            throw new FileProcessingException("This item is not rentable");
        }
        if (!game.isAvailable()) {
            throw new ItemNotFoundException("Game out of stock");
        }

        double rentalPrice = game.calculateRentalPrice(days);
        paymentProcessor.process(paymentMethod, rentalPrice);
        game.decreaseQuantity(1);

        Transaction transaction = new Transaction(game, customer, Transaction.Type.RENT, rentalPrice);
        transactionRepository.add(transaction);
    }

    public Product createAndAddProductInteractive(
            String type,
            String name,
            String description,
            int year,
            int quantity,
            double price,
            Scanner sc
    ) throws FileProcessingException {
        if (type == null) throw new IllegalArgumentException("Type cannot be null");
        if (sc == null) throw new IllegalArgumentException("Scanner cannot be null");

        Product created;

        switch (type) {
            case "DIGITAL" -> {
                System.out.print("Genre (e.g. ACTION): ");
                Game.GameGenre genre = Game.GameGenre.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Platform (e.g. PC / PlayStation / Xbox / NintendoSwitch): ");
                Game.GamePlatform platform = Game.GamePlatform.valueOf(sc.nextLine().trim());
                created = new DigitalGame(name, description, year, quantity, price, genre, platform);
            }
            case "PHYSICAL" -> {
                System.out.print("Price per day: ");
                double ppd = Double.parseDouble(sc.nextLine().trim());
                System.out.print("Genre (e.g. ACTION): ");
                Game.GameGenre genre = Game.GameGenre.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Platform (e.g. PC / PlayStation / Xbox / NintendoSwitch): ");
                Game.GamePlatform platform = Game.GamePlatform.valueOf(sc.nextLine().trim());
                created = new PhysicalGame(name, description, year, quantity, price, ppd, genre, platform);
            }
            case "CONSOLE" -> {
                System.out.print("Brand (SONY/MICROSOFT/NINTENDO/VALVE): ");
                Console.Brand brand = Console.Brand.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Model: ");
                String model = sc.nextLine().trim();
                created = new Console(name, description, year, quantity, price, brand, model);
            }
            case "DESKTOP" -> {
                System.out.print("Brand (DELL/HP/LENOVO/ASUS/ACER/CUSTOM): ");
                Desktop.Brand brand = Desktop.Brand.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Model: ");
                String model = sc.nextLine().trim();
                created = new Desktop(name, description, year, quantity, price, brand, model);
            }
            case "LAPTOP" -> {
                System.out.print("Brand (DELL/HP/LENOVO/ASUS/ACER/APPLE): ");
                Laptop.Brand brand = Laptop.Brand.valueOf(sc.nextLine().trim().toUpperCase());
                System.out.print("Model: ");
                String model = sc.nextLine().trim();
                System.out.print("Screen size: ");
                double screen = Double.parseDouble(sc.nextLine().trim());
                created = new Laptop(name, description, year, quantity, price, brand, model, screen);
            }
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }

        addProduct(created);
        return created;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(int id) throws ItemNotFoundException {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new ItemNotFoundException("Product with ID " + id + " not found");
        }
        return product;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerByEmail(String email) throws ItemNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new ItemNotFoundException("Customer with email " + email + " not found");
        }
        return customer;
    }

    public void buyProduct(int productId, Customer customer, PaymentMethod paymentMethod)
            throws ItemNotFoundException, PaymentException {
        Product product = findProductById(productId);
        if (!product.isAvailable()) {
            throw new ItemNotFoundException("Product out of stock");
        }

        double price = product.getPrice();
        paymentProcessor.process(paymentMethod, price);
        product.decreaseQuantity(1);

        Transaction transaction = new Transaction(product, customer, Transaction.Type.BUY, price);
        transactionRepository.add(transaction);
    }

    public void rentGame(int productId, Customer customer, PaymentMethod paymentMethod, int days)
            throws ItemNotFoundException, PaymentException, FileProcessingException {
        Product product = findProductById(productId);
        if (!(product instanceof Game game)) {
            throw new FileProcessingException("This item is not rentable");
        }
        if (!game.isAvailable()) {
            throw new ItemNotFoundException("Game out of stock");
        }

        double rentalPrice = game.calculateRentalPrice(days);
        paymentProcessor.process(paymentMethod, rentalPrice);
        game.decreaseQuantity(1);

        Transaction transaction = new Transaction(game, customer, Transaction.Type.RENT, rentalPrice);
        transactionRepository.add(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public double getTotalRevenue() {
        return transactionRepository.findAll().stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public Product findProductByName(String name) throws ItemNotFoundException {
        Product product = productRepository.findByName(name);
        if (product == null) {
            throw new ItemNotFoundException("Product with name \"" + name + "\" not found.");
        }
        return product;
    }

    public Customer findCustomerByName(String fullName) throws ItemNotFoundException {
        for (Customer c : customerRepository.findAll()) {
            String customerFullName = c.getName() + " " + c.getLastName();
            if (customerFullName.equalsIgnoreCase(fullName)) {
                return c;
            }
        }
        throw new ItemNotFoundException("Customer \"" + fullName + "\" not found.");
    }
}