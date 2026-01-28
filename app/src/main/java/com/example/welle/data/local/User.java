package com.example.welle.data.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "users")

public class User {

    @PrimaryKey
    @NonNull
    public String userId;   // Unique identifier for the user (from API)

    public String username;
    public String password;
    public String firstname;
    public String lastname;
    public String email;
    public String contact;
    public String usertype;

    public User(@NonNull String userId, String username, String password,
                String firstname, String lastname, String email,
                String contact, String usertype) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.usertype = usertype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}