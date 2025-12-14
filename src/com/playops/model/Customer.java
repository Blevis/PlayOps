package com.playops.model;

import com.playops.exceptions.*;
public class Customer {

    // Properties
    private static int nextId = 1;
    private final int id;
    private String name;
    private String lastName;
    private String email;
    private String address;

    // Constructor
    public Customer(String name, String lastName, String email, String address) {
        this.id = nextId++;
        setName(name);
        setLastName(lastName);
        setEmail(email);
        setAddress(address);
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("First name cannot be blank");
        this.name = name;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be blank");
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        if (!isValidEmail(email))
            throw new InvalidEmailException("Invalid email format. Use a valid format like address@domain.com");
        this.email = email;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank())
            throw new IllegalArgumentException("Address cannot be blank");
        this.address = address;
    }

    // Validation
    private boolean isValidEmail(String email){
        return email != null && email.contains("@") && email.contains(".");
    }

    // To String
    @Override
    public String toString() {
        return "[" + id + "] " + name + " " + lastName + " | " + email + " | " + address;
    }
}
