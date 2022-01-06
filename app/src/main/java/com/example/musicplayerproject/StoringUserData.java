package com.example.musicplayerproject;

public class StoringUserData {
    String username, phoneNo, email, password;

    public StoringUserData() {
    }

    public StoringUserData(String username, String phoneNo, String email, String password) {
        this.username = username;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
