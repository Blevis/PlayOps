package com.playops.controllers;

import com.playops.app.AppContext;
import com.playops.model.Product;
import com.playops.model.Transaction;
import com.playops.service.StoreService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DashboardController {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private enum ProductChartMode {
        TOP_10,
        ALL,
        LOW_STOCK
    }

    @FXML private Label totalSalesLabel;
    @FXML private Label totalProductsLabel;

    @FXML private LineChart<String, Number> salesChart;
    @FXML private BarChart<String, Number> productsChart;

    @FXML private ToggleGroup productsToggleGroup;
    @FXML private ToggleButton top10Toggle;
    @FXML private ToggleButton allToggle;
    @FXML private ToggleButton lowStockToggle;

    private StoreService storeService;
    private ProductChartMode productChartMode = ProductChartMode.TOP_10;

    @FXML
    public void initialize() {
        storeService = AppContext.getStoreService();

        if (top10Toggle != null) {
            top10Toggle.setSelected(true);
        }

        refreshData();
    }

    @FXML
    public void refreshData() {
        renderTotals();
        renderSalesChartLast14Days();
        renderProductsChart();
    }

    @FXML
    public void showTop10Products() {
        productChartMode = ProductChartMode.TOP_10;
        renderProductsChart();
    }

    @FXML
    public void showAllProducts() {
        productChartMode = ProductChartMode.ALL;
        renderProductsChart();
    }

    @FXML
    public void showLowStockProducts() {
        productChartMode = ProductChartMode.LOW_STOCK;
        renderProductsChart();
    }

    private void renderTotals() {
        double totalRevenue = storeService.getTotalRevenue();

        int totalStock = storeService.getAllProducts().stream()
                .mapToInt(Product::getQuantity)
                .sum();

        totalSalesLabel.setText(String.format("$%,.2f", totalRevenue));
        totalProductsLabel.setText(String.format("%,d", totalStock));
    }

    private void renderSalesChartLast14Days() {
        if (salesChart == null) return;

        Map<LocalDate, Double> revenueByDay = new TreeMap<>();
        for (Transaction t : storeService.getAllTransactions()) {
            LocalDate day = t.getTimestamp().toLocalDate();
            revenueByDay.merge(day, t.getAmount(), Double::sum);
        }

        LocalDate cutoff = LocalDate.now().minusDays(13);
        revenueByDay.entrySet().removeIf(e -> e.getKey().isBefore(cutoff));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (var e : revenueByDay.entrySet()) {
            series.getData().add(new XYChart.Data<>(e.getKey().toString(), e.getValue()));
        }

        salesChart.getData().setAll(series);

        // Tooltips: nodes are created after layout pass, so install in Platform.runLater
        Platform.runLater(() -> installSalesTooltips(series));
    }

    private void renderProductsChart() {
        if (productsChart == null) return;

        List<Product> all = storeService.getAllProducts();

        List<Product> shown = switch (productChartMode) {
            case TOP_10 -> all.stream()
                    .sorted(Comparator.comparingInt(Product::getQuantity).reversed())
                    .limit(10)
                    .toList();

            case ALL -> all.stream()
                    .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();

            case LOW_STOCK -> all.stream()
                    .filter(p -> p.getQuantity() > 0 && p.getQuantity() <= LOW_STOCK_THRESHOLD)
                    .sorted(Comparator.comparingInt(Product::getQuantity))
                    .toList();
        };

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Product p : shown) {
            series.getData().add(new XYChart.Data<>(p.getName(), p.getQuantity()));
        }

        productsChart.getData().setAll(series);

        Platform.runLater(() -> installProductTooltips(series));
    }

    private void installSalesTooltips(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getNode() == null) continue;

            String date = d.getXValue();
            double revenue = d.getYValue() == null ? 0.0 : d.getYValue().doubleValue();

            Tooltip tip = new Tooltip(String.format("Date: %s%nRevenue: $%,.2f", date, revenue));
            Tooltip.install(d.getNode(), tip);
        }
    }

    private void installProductTooltips(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> d : series.getData()) {
            if (d.getNode() == null) continue;

            String name = d.getXValue();
            int qty = d.getYValue() == null ? 0 : d.getYValue().intValue();

            Tooltip tip = new Tooltip(String.format("%s%nStock: %,d", name, qty));
            Tooltip.install(d.getNode(), tip);
        }
    }
}