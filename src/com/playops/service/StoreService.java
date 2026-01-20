package com.playops.service;

import com.playops.model.*;
import com.playops.repository.*;
import com.playops.exceptions.*;
import com.playops.interfaces.PaymentMethod;
import com.playops.payment.PaymentProcessor;
import java.util.List;

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