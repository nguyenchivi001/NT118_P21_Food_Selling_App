package com.example.food_selling_app.model;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String createdAt;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
