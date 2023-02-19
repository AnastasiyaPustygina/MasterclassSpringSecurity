package com.example.masterclassspringsecurity.domain;

public class User {

    private final String email;
    private final String fullName;
    private final String password;
    private final Role role;

    public User(String email, String fullName, String password, Role role) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
