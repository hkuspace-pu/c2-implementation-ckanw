package com.example.welle.data.repository;

import android.util.Log;

import com.example.welle.data.local.User;
import com.example.welle.data.local.UserDao;
import com.example.welle.api.ApiService;
import com.example.welle.data.remote.UserListResponse;
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

    // 從 API 抓取單一使用者並存入 Room
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

    // 新增：用 Email 查詢 (API + Room fallback)
    public void getUserByEmail(String studentId, String email, RepositoryCallback<User> callback) {
        Call<UserListResponse> call = apiService.getAllUsers(studentId);
        call.enqueue(new Callback<UserListResponse>() {
            @Override
            public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (UserResponse userResponse : response.body().getUsers()) {
                        if (userResponse.getEmail().equalsIgnoreCase(email)) {
                            User user = new User(
                                    "U" + System.currentTimeMillis(),
                                    userResponse.getUsername(),
                                    userResponse.getPassword(),
                                    userResponse.getFirstname(),
                                    userResponse.getLastname(),
                                    userResponse.getEmail(),
                                    userResponse.getContact(),
                                    userResponse.getUsertype()
                            );
                            callback.onSuccess(user);
                            return;
                        }
                    }
                    // API 沒找到 → fallback Room
                    User localUser = userDao.findUserByEmail(email);
                    if (localUser != null) {
                        callback.onSuccess(localUser);
                    } else {
                        callback.onFailure("User not found in API or Room");
                    }
                } else {
                    // API response 不成功 → fallback Room
                    User localUser = userDao.findUserByEmail(email);
                    if (localUser != null) {
                        callback.onSuccess(localUser);
                    } else {
                        callback.onFailure("API response error and no local record");
                    }
                }
            }

            @Override
            public void onFailure(Call<UserListResponse> call, Throwable t) {
                Log.e("UserRepository", "API call failed", t);
                // API error → fallback Room
                User localUser = userDao.findUserByEmail(email);
                if (localUser != null) {
                    callback.onSuccess(localUser);
                } else {
                    callback.onFailure("API error: " + t.getMessage());
                }
            }
        });
    }

    // 新增：註冊新使用者（API + Room）
    public void registerUser(String studentId, UserResponse newUser, RepositoryCallback<Boolean> callback) {
        Call<Void> call = apiService.createUser(studentId, newUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // 存入 Room
                    User localUser = new User(
                            "U" + System.currentTimeMillis(),
                            newUser.getUsername(),
                            newUser.getPassword(),
                            newUser.getFirstname(),
                            newUser.getLastname(),
                            newUser.getEmail(),
                            newUser.getContact(),
                            newUser.getUsertype()
                    );
                    new Thread(() -> userDao.insertUser(localUser)).start();
                    callback.onSuccess(true);
                } else {
                    callback.onFailure("Register failed via API");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("API error: " + t.getMessage());
            }
        });
    }


    // Repository callback interface
    public interface RepositoryCallback<T> {
        void onSuccess(T result);
        void onFailure(String errorMessage);
    }
}