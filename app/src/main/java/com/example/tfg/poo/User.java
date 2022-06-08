package com.example.tfg.poo;

public class User {

    private String id;
    private String email;
    private String username;
    private String icon;

    public User() {

    }

    public User(String id, String email, String username, String icon) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
