package com.example.welle.data.remote;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UserListResponse {

    @SerializedName("users")
    private List<UserResponse> users;

    // Getter
    public List<UserResponse> getUsers() {
        return users;
    }

    // Setter
    public void setUsers(List<UserResponse> users) {
        this.users = users;
    }
}