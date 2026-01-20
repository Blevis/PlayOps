package com.playops.repository;

import com.playops.model.Customer;
import com.playops.exceptions.FileProcessingException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CustomerRepository {
    private static final String CUSTOMER_FILE = "customers.csv";
    private Map<Integer, Customer> customersById = new HashMap<>();
    private Map<String, Customer> customersByEmail = new HashMap<>();
    private List<Customer> customersList = new ArrayList<>();

    public void save(List<Customer> customers) throws FileProcessingException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMER_FILE))) {
            writer.println("Id,Name,Last Name,Email,Address");
            for (Customer customer : customers) {
                writer.println(customer.getId() + "," + customer.getName() + "," +
                        customer.getLastName() + "," + customer.getEmail() + "," +
                        customer.getAddress());
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save customers file: " + e.getMessage());
        }
    }

    public List<Customer> load() throws FileProcessingException {
        customersById.clear();
        customersByEmail.clear();
        customersList.clear();

        if (!Files.exists(Paths.get(CUSTOMER_FILE))) {
            return customersList;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(CUSTOMER_FILE));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                Customer customer = parseCustomerLine(line);
                if (customer != null) {
                    customersList.add(customer);
                    customersById.put(customer.getId(), customer);
                    customersByEmail.put(customer.getEmail().toLowerCase(), customer);
                }
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load customers file: " + e.getMessage());
        }
        return customersList;
    }

    public Customer findById(int id) {
        return customersById.get(id);
    }

    public Customer findByEmail(String email) {
        return customersByEmail.get(email.toLowerCase());
    }

    public List<Customer> findAll() {
        return new ArrayList<>(customersList);
    }

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
            return null;
        }
    }
}