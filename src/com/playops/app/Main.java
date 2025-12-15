package com.playops.app;

import com.playops.model.*;
import com.playops.store.Store;
import com.playops.payment.*;
import com.playops.interfaces.PaymentMethod;
import com.playops.exceptions.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    Welcome to PlayOps Store System");
        System.out.println("========================================\n");
        try {
            // ============================================
            // 1. CREATE STORE
            // ============================================
            Store store = new Store("PlayOps");

            // ============================================
            // 2. LOAD EXISTING DATA (On Application Start)
            // ============================================
            System.out.println(">>> Loading existing data from files...");
            store.loadInventory();
            store.loadCustomers();
            store.loadTransactions();
            System.out.println();

            // ============================================
            // 3. ADD CUSTOMERS (If not already loaded)
            // ============================================
            System.out.println(">>> Adding customers...");
            Customer customer1 = new Customer("Blevis", "Allushi", "blevis.allushi24@umt.edu.al", "123 Main St");
            Customer customer2 = new Customer("Kristjan", "Seraj", "kristjan.seraj24@umt.edu.al", "456 Elm St");
            Customer customer3 = new Customer("Alice", "Johnson", "alice.johnson@example.com", "789 Oak Ave");
            Customer customer4 = new Customer("Evis", "Plaku", "evis.plaku@umt.edu.al", "Sand Hill Road");

            store.addCustomer(customer1);
            store.addCustomer(customer2);
            store.addCustomer(customer3);
            store.addCustomer(customer4);
            System.out.println();

            // ============================================
            // 4. ADD PRODUCTS (Demonstrating All Types)
            // ============================================
            System.out.println(">>> Adding products to inventory...");

            // Digital Games
            DigitalGame digitalGame1 = new DigitalGame(
                    "CyberQuest",
                    "Sci-fi adventure",
                    2025,
                    5,
                    50.00,
                    Game.GameGenre.ACTION,
                    Game.GamePlatform.PC
            );

            DigitalGame digitalGame2 = new DigitalGame(
                    "Mystic Realm",
                    "Fantasy adventure",
                    2024,
                    3,
                    45.15,
                    Game.GameGenre.ADVENTURE,
                    Game.GamePlatform.PlayStation
            );

            // Physical Games (Rentable)
            PhysicalGame physicalGame1 = new PhysicalGame(
                    "FantasyLand",
                    "Fantasy RPG",
                    2024,
                    3,
                    60.00,
                    7.00,
                    Game.GameGenre.ROLE_PLAYING,
                    Game.GamePlatform.PC
            );

            PhysicalGame physicalGame2 = new PhysicalGame(
                    "Racing Legends",
                    "High-speed racing game",
                    2023,
                    2,
                    55.00,
                    8.00,
                    Game.GameGenre.RACING,
                    Game.GamePlatform.Xbox
            );

            // Consoles
            Console ps5 = new Console(
                    "PlayStation 5",
                    "Latest Sony console",
                    2023,
                    2,
                    500.00,
                    Console.Brand.SONY,
                    "PS5 Standard"
            );

            Console xbox = new Console(
                    "Xbox Series X",
                    "Microsoft gaming console",
                    2023,
                    1,
                    450.00,
                    Console.Brand.MICROSOFT,
                    "Series X"
            );

            // Desktops
            Desktop gamingDesktop = new Desktop(
                    "Gaming PC Pro",
                    "High-performance gaming desktop",
                    2024,
                    1,
                    1200.00,
                    Desktop.Brand.DELL,
                    "Alienware X15"
            );

            // Laptops
            Laptop gamingLaptop = new Laptop(
                    "Gaming Laptop Elite",
                    "Portable gaming laptop",
                    2024,
                    2,
                    1500.00,
                    Laptop.Brand.ASUS,
                    "ROG Strix",
                    17.3
            );

            Laptop macbook = new Laptop(
                    "MacBook Pro",
                    "Apple professional laptop",
                    2024,
                    1,
                    2000.00,
                    Laptop.Brand.APPLE,
                    "M3 Pro",
                    16.0
            );

            // Add all products to the inventory
            store.addProduct(digitalGame1);
            store.addProduct(digitalGame2);
            store.addProduct(physicalGame1);
            store.addProduct(physicalGame2);
            store.addProduct(ps5);
            store.addProduct(xbox);
            store.addProduct(gamingDesktop);
            store.addProduct(gamingLaptop);
            store.addProduct(macbook);
            System.out.println();

            // ============================================
            // 5. DISPLAY INITIAL STATE
            // ============================================
            System.out.println(">>> Current Store State:");
            store.displayInventory();
            store.displayCustomers();
            System.out.println();

            // ============================================
            // 6. DEMONSTRATE PURCHASES (Buying Products)
            // ============================================
            System.out.println(">>> Processing Purchases...");

            // Purchase 1: Digital Game with Cash
            System.out.println("\n--- Purchase 1: Digital Game (Cash) ---");
            PaymentMethod cashPayment1 = new CashPayment(100.00);
            store.buyProduct(digitalGame1.getId(), customer1, cashPayment1);

            // Purchase 2: Console with Card
            System.out.println("\n--- Purchase 2: Console (Card) ---");
            PaymentMethod cardPayment1 = new CardPayment("1234567890123456", "Kristjan Seraj");
            store.buyProduct(ps5.getId(), customer2, cardPayment1);

            // Purchase 3: Laptop with Card
            System.out.println("\n--- Purchase 3: Laptop (Card) ---");
            PaymentMethod cardPayment2 = new CardPayment("9876543210987654", "Alice Johnson");
            store.buyProduct(gamingLaptop.getId(), customer3, cardPayment2);

            System.out.println();

            // ============================================
            // 7. DEMONSTRATE RENTALS
            // ============================================
            System.out.println(">>> Processing Rentals...");

            // Rental 1: Physical Game for 3 days
            System.out.println("\n--- Rental 1: Physical Game (3 days) ---");
            PaymentMethod rentCash1 = new CashPayment(50.00);
            store.rentGame(physicalGame1.getId(), customer4, rentCash1, 3);

            // Rental 2: Physical Game for 5 days
            System.out.println("\n--- Rental 2: Physical Game (5 days) ---");
            PaymentMethod rentCash2 = new CashPayment(60.00);
            store.rentGame(physicalGame2.getId(), customer2, rentCash2, 5);

            System.out.println();

            // ============================================
            // 8. DISPLAY UPDATED STATE
            // ============================================
            System.out.println(">>> Updated Store State:");
            store.displayInventory();
            System.out.println();
            store.displayTransactions();
            System.out.println();

            // ============================================
            // 9. DEMONSTRATE SEARCH FUNCTIONALITY
            // ============================================
            System.out.println(">>> Searching for Products and Customers...");

            try {
                Product foundProduct = store.findProductByName("FantasyLand");
                System.out.println("Found product: " + foundProduct);

                Customer foundCustomer = store.findCustomerByName("Evis Plaku");
                System.out.println("Found customer: " + foundCustomer);
            } catch (ItemNotFoundException e) {
                System.out.println("Search error: " + e.getMessage());
            }
            System.out.println();

            // ============================================
            // 10. DISPLAY BUSINESS METRICS
            // ============================================
            System.out.println(">>> Business Metrics:");
            System.out.println("Total Revenue: $" + String.format("%.2f", store.getTotalRevenue()));
            System.out.println("Total Products in Inventory: " + store.getInventory().size());
            System.out.println("Total Customers: " + store.getCustomers().size());
            System.out.println("Total Transactions: " + store.getTransactions().size());
            System.out.println();

            // ============================================
            // 11. DEMONSTRATE VALIDATION (Error Handling)
            // ============================================
            System.out.println(">>> Demonstrating Input Validation...");

            // Try to create customer with invalid email
            try {
                System.out.println("\n--- Attempting to create customer with invalid email ---");
                Customer invalidCustomer = new Customer("Test", "User", "invalid-email", "123 St");
            } catch (InvalidEmailException e) {
                System.out.println("Validation caught: " + e.getMessage());
            }

            // Try to buy a product with insufficient cash
            try {
                System.out.println("\n--- Attempting purchase with insufficient cash ---");
                PaymentMethod insufficientCash = new CashPayment(10.00);
                store.buyProduct(digitalGame2.getId(), customer1, insufficientCash);
            } catch (PaymentException e) {
                System.out.println("Payment validation caught: " + e.getMessage());
            }

            // Try to find a non-existent product
            try {
                System.out.println("\n--- Attempting to find non-existent product ---");
                store.findProductByName("NonExistentGame");
            } catch (ItemNotFoundException e) {
                System.out.println("Item not found handled: " + e.getMessage());
            }

            System.out.println();

            // ============================================
            // 12. SAVE ALL DATA (On Application Exit)
            // ============================================
            System.out.println(">>> Saving all data to files...");
            store.saveInventory();
            store.saveCustomers();
            store.saveTransactions();
            System.out.println();

            System.out.println("========================================");
            System.out.println("    Application completed successfully!");
            System.out.println("========================================");

        } catch (ItemNotFoundException e) {
            System.out.println("Error: Item not found - " + e.getMessage());
        } catch (PaymentException e) {
            System.out.println("Error: Payment failed - " + e.getMessage());
        } catch (FileProcessingException e) {
            System.out.println("Error: File operation failed - " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Validation failed - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("  Caused by: " + e.getCause().getMessage());
            }
        }
    }
}
