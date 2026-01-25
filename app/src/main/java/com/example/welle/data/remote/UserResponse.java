package com.example.welle.data.remote;

import com.google.gson.annotations.SerializedName;

public class UserResponse {

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("email")
    private String email;

    @SerializedName("contact")
    private String contact;

    @SerializedName("usertype")
    private String usertype;

    // --- Getter methods ---
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public String getUsertype() {
        return usertype;
    }

    // --- Setter methods ---
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}