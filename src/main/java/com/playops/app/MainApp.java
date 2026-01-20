package com.playops.app;

import com.playops.controllers.AdminLoginController;
import com.playops.controllers.DashboardController;
import com.playops.service.StoreService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private StoreService storeService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        storeService = new StoreService("PlayOps");
        storeService.initialize(); // load data

        TabPane tabPane = new TabPane();

        // --- Admin Login Tab ---
        Tab loginTab = new Tab("Admin Login");
        loginTab.setContent(AdminLoginController.createContent(storeService, tabPane));
        loginTab.setClosable(false);

        // --- Dashboard Tab ---
        Tab dashboardTab = new Tab("Dashboard");
        dashboardTab.setContent(DashboardController.createContent(storeService));
        dashboardTab.setClosable(false);

        tabPane.getTabs().addAll(loginTab, dashboardTab);

        // Disable dashboard tab until login
        dashboardTab.setDisable(true);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("PlayOps Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        storeService.shutdown(); // save data
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
