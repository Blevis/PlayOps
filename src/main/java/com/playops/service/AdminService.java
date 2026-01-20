package com.playops.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class AdminService {

    private static final Path ADMIN_FILE = Paths.get("admins.csv");

    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = "root";

    private final Map<String, String> passwordByUsername = new HashMap<>();

    public AdminService() {
        loadOrInitialize();
    }

    public AuthResult authenticate(String usernameRaw, String passwordRaw) {
        String username = normalizeUsername(usernameRaw);
        String password = passwordRaw == null ? "" : passwordRaw;

        String expectedPassword = passwordByUsername.get(username);
        if (expectedPassword == null) {
            return AuthResult.invalid("Invalid username or password");
        }

        if (expectedPassword.equals(password)) {
            return AuthResult.success(username, "ADMIN");
        }

        return AuthResult.invalid("Invalid username or password");
    }

    // -------------------- Persistence --------------------

    private void loadOrInitialize() {
        passwordByUsername.clear();
        List<String> lines = new ArrayList<>();
        lines.add("username,password");
        lines.add(ROOT_USERNAME + "," + ROOT_PASSWORD);

        try {
            Files.write(
                    ADMIN_FILE,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write admins.csv: " + e.getMessage(), e);
        }

        passwordByUsername.put(ROOT_USERNAME, ROOT_PASSWORD);
    }

    private String normalizeUsername(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }

    // -------------------- DTOs --------------------

    public record AuthResult(boolean success, boolean locked, String message, String username, String role) {
        public static AuthResult success(String username, String role) {
            return new AuthResult(true, false, null, username, role);
        }

        public static AuthResult locked(String message) {
            return new AuthResult(false, true, message, null, null);
        }

        public static AuthResult invalid(String message) {
            return new AuthResult(false, false, message, null, null);
        }
    }
}