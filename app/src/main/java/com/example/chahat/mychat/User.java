package com.example.chahat.mychat;

public class User {

    String name, email, password;
    long phone;

    public User(String name, long phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
   // public int getAge(){return age;}

    public User(String email, String password) {
        this("", -1, email, password);
    }
}
