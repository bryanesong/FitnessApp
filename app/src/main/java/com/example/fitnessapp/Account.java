package com.example.fitnessapp;

public class Account {
    private  String email;
    private String  password;
    private String userName;

    public Account(){
        //123
    }

    public String getUsername(){
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String userName){
        this.userName = userName;
    }
}
