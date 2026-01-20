package com.playops.repository;

import com.playops.model.*;
import com.playops.exceptions.FileProcessingException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProductRepository {
    private static final String INVENTORY_FILE = "inventory.csv";
    private Map<Integer, Product> productsById = new HashMap<>();
    private List<Product> productsList = new ArrayList<>();

    public void save(List<Product> products) throws FileProcessingException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INVENTORY_FILE))) {
            writer.println("Id,Type,Name,Description,Year,Quantity,Price,Other infos");
            for (Product product : products) {
                writer.println(productToString(product));
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to save inventory file: " + e.getMessage());
        }
    }

    public List<Product> load() throws FileProcessingException {
        productsById.clear();
        productsList.clear();

        if (!Files.exists(Paths.get(INVENTORY_FILE))) {
            return productsList;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(INVENTORY_FILE));
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.trim().isEmpty()) continue;
                try {
                    Product product = parseProductLine(line);
                    if (product != null) {
                        productsList.add(product);
                        productsById.put(product.getId(), product);
                    }
                } catch (FileProcessingException e) {
                    throw new FileProcessingException("Invalid line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new FileProcessingException("Failed to load inventory file: " + e.getMessage());
        }
        return productsList;
    }

    public Product findById(int id) {
        return productsById.get(id);
    }

    public Product findByName(String name) {
        for (Product p : productsList) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    public List<Product> findAll() {
        return new ArrayList<>(productsList);
    }

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
}