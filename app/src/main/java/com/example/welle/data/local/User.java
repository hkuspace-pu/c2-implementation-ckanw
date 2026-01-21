package com.example.welle.data.local;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "user_id")
    public String userId;   // Unique identifier for the user (from server)

    @ColumnInfo(name = "username")
    public String username; // Username

    @ColumnInfo(name = "password")
    public String password; // Password (usually hashed on server)

    @ColumnInfo(name = "firstname")
    public String firstname; // First name

    @ColumnInfo(name = "lastname")
    public String lastname;  // Last name

    @ColumnInfo(name = "email", index = true)
    public String email;     // Email address

    @ColumnInfo(name = "contact")
    public String contact;   // Contact number

    @ColumnInfo(name = "usertype")
    public String usertype;  // Type of user (e.g., student, admin)

    // Local-only field: not part of server schema
    @ColumnInfo(name = "is_synced")
    public boolean isSynced; // Whether this record has been synced with server

    public User(@NonNull String userId, String username, String password,
                String firstname, String lastname, String email,
                String contact, String usertype, boolean isSynced) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.contact = contact;
        this.usertype = usertype;
        this.isSynced = isSynced;
    }
}