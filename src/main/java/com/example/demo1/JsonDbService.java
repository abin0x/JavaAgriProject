package com.example.demo1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class JsonDbService {

    // Save the file in the user's home folder so it persists forever
    private static final String FILE_NAME = "smart_krishi_users.json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final File databaseFile;

    public JsonDbService() throws IOException {
        // 1. Get the path to C:\Users\DELL\smart_krishi_users.json
        String userHome = System.getProperty("user.home");
        this.databaseFile = new File(userHome, FILE_NAME);

        // 2. If the file doesn't exist yet, create it with an empty list
        if (!databaseFile.exists()) {
            databaseFile.createNewFile();
            saveUsers(new ArrayList<>()); // Initialize with empty array []

            // Optional: Create the default Admin user on first run
            createDefaultAdmin();
        }
    }

    private void createDefaultAdmin() throws IOException {
        List<User> users = new ArrayList<>();
        // Password "1234" hashed
        String adminHash = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3";
        users.add(new User("Admin User", "admin@example.com", "01700000000", "admin", adminHash));
        saveUsers(users);
        System.out.println("Database created at: " + databaseFile.getAbsolutePath());
    }

    // --- Core Read/Write Methods ---

    public List<User> loadUsers() throws IOException {
        if (!databaseFile.exists() || databaseFile.length() == 0) {
            return new ArrayList<>();
        }
        return mapper.readValue(databaseFile, new TypeReference<List<User>>() {});
    }

    private void saveUsers(List<User> users) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(databaseFile, users);
    }

    // --- Logic Methods ---

    public boolean registerUser(User newUser) throws IOException {
        List<User> users = loadUsers();
        // Check duplicate mobile or name
        boolean exists = users.stream().anyMatch(u ->
                u.getMobile().equals(newUser.getMobile()) ||
                        u.getName().equalsIgnoreCase(newUser.getName())
        );

        if (exists) {
            return false;
        }
        users.add(newUser);
        saveUsers(users);
        return true;
    }

    public User loginUser(String input, String rawPassword) throws IOException {
        List<User> users = loadUsers();
        String hashedPassword = hashPassword(rawPassword);

        return users.stream()
                .filter(u ->
                        (u.getMobile().equals(input) ||
                                u.getUsername().equals(input) ||
                                u.getEmail().equals(input) ||
                                u.getName().equalsIgnoreCase(input))
                                && u.getPasswordHash().equals(hashedPassword)
                )
                .findFirst()
                .orElse(null);
    }

    // --- Security Helper ---
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}