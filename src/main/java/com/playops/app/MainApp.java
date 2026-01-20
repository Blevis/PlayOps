package com.playops.app;

import com.playops.model.Console;
import com.playops.model.Customer;
import com.playops.model.DigitalGame;
import com.playops.model.Game;
import com.playops.model.PhysicalGame;
import com.playops.model.Product;
import com.playops.payment.CashPayment;
import com.playops.service.StoreService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainApp extends Application {

    private StoreService storeService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        storeService = new StoreService("PlayOps");
        storeService.initialize();
        AppContext.setStoreService(storeService);

        // ---- DEBUG: show where CSVs are being loaded from + counts ----
        Path cwd = Paths.get("").toAbsolutePath();
        System.out.println("=== PlayOps Startup ===");
        System.out.println("Working directory: " + cwd);
        System.out.println("customers.csv exists? " + cwd.resolve("customers.csv").toFile().exists());
        System.out.println("inventory.csv exists? " + cwd.resolve("inventory.csv").toFile().exists());
        System.out.println("transactions.csv exists? " + cwd.resolve("transactions.csv").toFile().exists());
        System.out.println("Loaded customers: " + storeService.getAllCustomers().size());
        System.out.println("Loaded products: " + storeService.getAllProducts().size());
        System.out.println("Loaded transactions: " + storeService.getAllTransactions().size());
        System.out.println("=======================");
        // -------------------------------------------------------------

        // Seed only if inventory is empty (prevents wiping leading to empty dashboard)
        if (storeService.getAllProducts().isEmpty()) {
            Customer c1 = new Customer("Alice", "Johnson", "alice.johnson@example.com", "12 Maple Street");
            Customer c2 = new Customer("Bob", "Smith", "bob.smith@example.com", "8 Oak Avenue");

            Product p1 = new Console(
                    "PlayStation 5",
                    "Disc edition console",
                    2020,
                    8,
                    499.99,
                    Console.Brand.SONY,
                    "PS5"
            );

            Product p2 = new DigitalGame(
                    "Elden Ring",
                    "Open-world action RPG",
                    2022,
                    40,
                    59.99,
                    Game.GameGenre.ACTION,
                    Game.GamePlatform.PC
            );

            Product p3 = new PhysicalGame(
                    "Mario Kart 8 Deluxe",
                    "Racing game (cartridge)",
                    2017,
                    12,
                    49.99,
                    3.50,
                    Game.GameGenre.RACING,
                    Game.GamePlatform.NintendoSwitch
            );

            storeService.addCustomer(c1);
            storeService.addCustomer(c2);

            storeService.addProduct(p1);
            storeService.addProduct(p2);
            storeService.addProduct(p3);

            storeService.buyProductByName("Elden Ring", c1, new CashPayment(1000.00));
            storeService.buyProductByName("PlayStation 5", c2, new CashPayment(1000.00));
            storeService.rentGameByName("Mario Kart 8 Deluxe", c1, new CashPayment(1000.00), 3);

            storeService.saveNow();

            System.out.println("Seeded demo data (inventory was empty).");
        }

        URL fxmlLocation = getClass().getResource("/ui/login/login.fxml");
        if (fxmlLocation == null) {
            throw new RuntimeException("FXML file not found at /ui/login/login.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();
        Scene scene = new Scene(root);

        URL cssLocation = getClass().getResource("/ui/css/style.css");
        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
        }

        primaryStage.setTitle("PlayOps Admin Dashboard");

        var iconStream = getClass().getResourceAsStream("/ui/images/icon.png");
        if (iconStream != null) {
            primaryStage.getIcons().add(new Image(iconStream));
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (storeService != null) {
            storeService.shutdown();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}