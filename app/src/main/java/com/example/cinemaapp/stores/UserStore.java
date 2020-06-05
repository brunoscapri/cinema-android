package com.example.cinemaapp.stores;

public class UserStore {
    private UserStore(){}
    private static UserStore instance = new UserStore();
    private String userUID;
    private String email;

    public static UserStore getInstance() {
        return instance;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID ;
    }

    public String getUserUID() {
        return userUID;
    }

    public String getEmail() {
        return email;
    }
}
