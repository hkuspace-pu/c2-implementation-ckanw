package com.example.welle.data.repository;

import android.util.Log;

import com.example.welle.data.local.User;
import com.example.welle.data.local.UserDao;
import com.example.welle.api.ApiService;
import com.example.welle.data.remote.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private final ApiService apiService;
    private final UserDao userDao;

    public UserRepository(ApiService apiService, UserDao userDao) {
        this.apiService = apiService;
        this.userDao = userDao;
    }

    // 從 API 抓取使用者並存入 Room
    public void fetchAndStoreUser(String studentId, String userId) {
        Call<UserResponse> call = apiService.getUser(studentId, userId);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse userResponse = response.body();

                    User user = new User(
                            userId,
                            userResponse.getUsername(),
                            userResponse.getPassword(),
                            userResponse.getFirstname(),
                            userResponse.getLastname(),
                            userResponse.getEmail(),
                            userResponse.getContact(),
                            userResponse.getUsertype()
                    );

                    // 存入 Room
                    new Thread(() -> userDao.insertUser(user)).start();

                    Log.d("UserRepository", "User saved: " + user.username);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("UserRepository", "API call failed", t);
            }
        });
    }
}