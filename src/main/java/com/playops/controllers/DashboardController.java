package com.playops.controllers;

import com.playops.service.StoreService;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {

    public static Node createContent(StoreService storeService) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label title = new Label("PlayOps Admin Dashboard");

        double totalRevenue = storeService.getTotalRevenue();
        int totalProducts = storeService.getAllProducts().size();
        int totalCustomers = storeService.getAllCustomers().size();
        int totalTransactions = storeService.getAllTransactions().size();

        Label revenueLabel = new Label("Total Revenue: $" + String.format("%.2f", totalRevenue));
        Label productsLabel = new Label("Total Products: " + totalProducts);
        Label customersLabel = new Label("Total Customers: " + totalCustomers);
        Label transactionsLabel = new Label("Total Transactions: " + totalTransactions);

        layout.getChildren().addAll(title, revenueLabel, productsLabel, customersLabel, transactionsLabel);

        return layout;
    }
}
