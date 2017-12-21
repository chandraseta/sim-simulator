package com.thesim.simsimulator.model;

public class User {
    private static User user = null;
    public String id;
    public String email;

    private User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public static User User(String id, String email) {
        if (user == null) {
            user = new User(id, email);
        }
        return user;
    }

    public static User getInstance() {
        return user;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
