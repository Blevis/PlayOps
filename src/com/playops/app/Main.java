package com.playops.app;

import com.playops.model.*;
import com.playops.store.Store;
import com.playops.payment.*;
import com.playops.interfaces.PaymentMethod;
import com.playops.exceptions.*;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Create Store
            Store store = new Store("PlayOps");

            // 2. Add Customers
            Customer blevis = new Customer("Blevis", "Allushi", "blevis@example.com", "123 Main St");
            Customer kristi = new Customer("Kristjan", "Seraj", "kristi@example.com", "456 Elm St");
            store.addCustomer(blevis);
            store.addCustomer(kristi);

            // 3. Add Products
            DigitalGame digitalGame = new DigitalGame(
                    "CyberQuest",
                    "Sci-fi adventure",
                    2025,
                    5,
                    50,
                    Game.GameGenre.ACTION,
                    Game.GamePlatform.PC
            );

            PhysicalGame physicalGame = new PhysicalGame(
                    "FantasyLand",
                    "Fantasy RPG",
                    2024,
                    3,
                    60,
                    7,
                    Game.GameGenre.ROLE_PLAYING,
                    Game.GamePlatform.PC
            );

            Console ps5 = new Console(
                    "PlayStation 5",
                    "Latest Sony console",
                    2023,
                    2,
                    500,
                    Console.Brand.SONY,
                    "PS5 Standard"
            );

            store.addProduct(digitalGame);
            store.addProduct(physicalGame);
            store.addProduct(ps5);

            // 4. Display initial inventory and customers
            store.displayInventory();
            store.displayCustomers();

            // 5. Buying a product
            PaymentMethod cashPayment = new CashPayment(100); // enough for digitalGame
            store.buyProduct(digitalGame.getId(), blevis, cashPayment);

            PaymentMethod cardPayment = new CardPayment("1234567890123456", "Kristjan Seraj");
            store.buyProduct(ps5.getId(), kristi, cardPayment);

            // 6. Renting a physical game
            PaymentMethod rentCash = new CashPayment(50);
            store.rentGame(physicalGame.getId(), blevis, rentCash, 3); // 3-day rental

            // 7. Display updated inventory, transactions
            store.displayInventory();
            store.displayTransactions();

            // 8. Find products/customers by name
            Product foundGame = store.findProductByName("CyberQuest");
            Customer foundCustomer = store.findCustomerByName("Kristjan Seraj");
            System.out.println("Found product: " + foundGame);
            System.out.println("Found customer: " + foundCustomer);

            // 9. Display total revenue
            System.out.println("Total revenue: $" + store.getTotalRevenue());

        } catch (ItemNotFoundException | PaymentException | FileProcessingException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}
