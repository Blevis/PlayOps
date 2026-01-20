package com.playops.app;

import com.playops.service.StoreService;

public final class AppContext {
    private static StoreService storeService;

    private AppContext() {}

    public static StoreService getStoreService() {
        if (storeService == null) {
            throw new IllegalStateException("StoreService not initialized. Call AppContext.setStoreService(...) in MainApp.");
        }
        return storeService;
    }

    public static void setStoreService(StoreService storeService) {
        AppContext.storeService = storeService;
    }
}