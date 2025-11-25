package com.example.demo1;

public class User {
    private String name;
    private String email;
    private String mobile;
    private String username;
    private String passwordHash;

    // Default no-argument constructor (required by Jackson)
    public User() {
    }

    // Constructor for new users
    public User(String name, String email, String mobile, String username, String passwordHash) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // --- Getters and Setters ---
    // You must include getters for Jackson to read the data

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}