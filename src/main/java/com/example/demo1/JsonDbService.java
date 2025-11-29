package com.example.demo1;
// প্যাকেজ আপনার ফাইল লোকেশন অনুযায়ী ঠিক করুন: com.example.demo1 বা com.example.demo1.marketfruits

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class JsonDbService {

    // 🛑 ফিক্স ১: সঠিক আপেক্ষিক পাথ 🛑
    private static final String FILE_PATH = "src/main/resources/com/example/demo1/user_data.json";

    private final ObjectMapper mapper = new ObjectMapper();
    private final File databaseFile;

    public JsonDbService() throws IOException {
        this.databaseFile = new File(FILE_PATH);

        if (!databaseFile.exists() || databaseFile.length() == 0) {
            // ডিরেক্টরি তৈরি নিশ্চিত করা
            databaseFile.getParentFile().mkdirs();
            saveUsers(new ArrayList<>());
            createDefaultAdmin();
        }
        System.out.println("DB location: " + databaseFile.getAbsolutePath());
    }

    private void createDefaultAdmin() throws IOException {
        List<User> users = loadUsers();
        if (users.isEmpty() || users.stream().noneMatch(u -> u.getUsername().equals("admin"))) {
            String adminHash = "a94a8fe5ccb19ba61c4c0873d391e987982fbbd3"; // Hash for "1234"
            users.add(new User("Admin User", "admin@example.com", "01700000000", "admin", adminHash));
            saveUsers(users);
            System.out.println("Default Admin created/re-created.");
        }
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
        boolean exists = users.stream().anyMatch(u ->
                u.getMobile().equals(newUser.getMobile()) ||
                        u.getUsername().equalsIgnoreCase(newUser.getUsername())
        );

        if (exists) {
            return false;
        }
        users.add(newUser);
        saveUsers(users);
        return true;
    }

    // JsonDbService.java (loginUser method)

    public User loginUser(String input, String rawPassword) throws IOException {
        List<User> users = loadUsers();
        String hashedPassword = hashPassword(rawPassword);

        return users.stream()
                .filter(u ->
                        (u.getMobile().equals(input) ||
                                u.getUsername().equals(input) ||
                                u.getEmail().equals(input) ||
                                u.getName().equalsIgnoreCase(input)) // 🛑 এই লাইনটি যুক্ত করুন 🛑
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