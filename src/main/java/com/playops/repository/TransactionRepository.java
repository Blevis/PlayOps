package com.playops.repository;

import com.playops.model.*;
import com.playops.exceptions.FileProcessingException;
import com.playops.service.StoreService;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionRepository {
    private static final String TRANSACTIONS_FILE = "transactions.csv";
    private List<Transaction> transactions = new ArrayList<>();
    private StoreService storeService;

    public void setStoreService(StoreService storeService) {
        this.storeService = storeService;
    }

    public void save(List<Transaction> transactions) throws FileProcessingException {
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
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save transaction file: " + e.getMessage());
        }
        this.transactions = new ArrayList<>(transactions);
    }

    public List<Transaction> load() throws FileProcessingException {
        transactions.clear();

        if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) {
            return transactions;
        }

        if (storeService == null) {
            throw new IllegalStateException("StoreService must be set before loading transactions");
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(TRANSACTIONS_FILE));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                Transaction transaction = parseTransactionLine(line);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load transaction file: " + e.getMessage());
        }

        return transactions;
    }

    public void add(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    private Transaction parseTransactionLine(String line) {
        String[] parts = line.split(",");
        if (parts.length != 5) return null;

        try {
            Transaction.Type type = Transaction.Type.valueOf(parts[0].trim());
            String customerName = parts[1].trim();
            String productName = parts[2].trim();
            double amount = Double.parseDouble(parts[3].trim());

            LocalDateTime timestamp = LocalDateTime.parse(parts[4].trim(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            Product product = storeService.findProductByName(productName);
            Customer customer = storeService.findCustomerByName(customerName);

            return new Transaction(product, customer, type, amount, timestamp);
        } catch (Exception e) {
            return null;
        }
    }
}