package com.example.bk_home_smarter.src.models;

public class User {
    public String userId;
    public String username;
    public String password;
    public String displayName;

    public User(){

    }

    public User(String userId, String username, String password, String displayName){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }
}
