package com.example.welle.ui.staff;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.api.ApiClient;
import com.example.welle.api.ApiService;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.User;
import com.example.welle.data.local.UserDao;
import com.example.welle.data.repository.UserRepository;
import com.example.welle.data.remote.UserResponse;

import java.util.UUID;

public class StaffLoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnLogin, btnBack;
    private AppDatabase db;
    private UserDao userDao;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_login);

        editEmail = findViewById(R.id.editTextStaffemail);
        editPassword = findViewById(R.id.editTextStaffPassword);
        btnLogin = findViewById(R.id.btnstaffok);
        btnBack = findViewById(R.id.btnback);

        db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        userRepository = new UserRepository(apiService, userDao);

        btnBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim().toLowerCase();
            String password = editPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNetworkAvailable()) {
                // 🔹 Offline
                new Thread(() -> {
                    User localUser = userDao.findUserByEmailAndType(email, "staff");
                    runOnUiThread(() -> {
                        if (localUser != null && localUser.password.equals(password)) {
                            Toast.makeText(this, "Offline staff login success", Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(this, StaffMainActivity.class));
                        } else if (localUser == null) {
                            // 離線新增 staff
                            User newUser = new User(
                                    UUID.randomUUID().toString(),
                                    "staffUser",
                                    password,
                                    "",
                                    "",
                                    email,
                                    "",
                                    "staff"
                            );
                            new Thread(() -> userDao.insertUser(newUser)).start();
                            Toast.makeText(this, "Offline registered staff: " + email, Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(this, StaffMainActivity.class));
                        } else {
                            Toast.makeText(this, "Invalid staff credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            } else {
                // 🔹 Online
                userRepository.getUserByEmail("staff_api", email, new UserRepository.RepositoryCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        if (result.password.equals(password) && "staff".equals(result.usertype)) {
                            new Thread(() -> {
                                User existingUser = userDao.findUserByEmailAndType(email, "staff");
                                if (existingUser == null) {
                                    userDao.insertUser(result);
                                } else {
                                    existingUser.password = result.password;
                                    existingUser.firstname = result.firstname;
                                    existingUser.lastname = result.lastname;
                                    existingUser.contact = result.contact;
                                    userDao.updateUser(existingUser);
                                }
                            }).start();

                            Toast.makeText(StaffLoginActivity.this, "API staff login success", Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(StaffLoginActivity.this, StaffMainActivity.class));
                        } else {
                            Toast.makeText(StaffLoginActivity.this, "Invalid staff credentials", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        // API 沒找到 → 註冊新 staff
                        UserResponse newUserResponse = new UserResponse();
                        newUserResponse.setUsername("staffUser");
                        newUserResponse.setPassword(password);
                        newUserResponse.setFirstname("");
                        newUserResponse.setLastname("");
                        newUserResponse.setEmail(email);
                        newUserResponse.setContact("");
                        newUserResponse.setUsertype("staff");

                        userRepository.registerUser("staff_api", newUserResponse, new UserRepository.RepositoryCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean result) {
                                new Thread(() -> {
                                    User existingUser = userDao.findUserByEmailAndType(email, "staff");
                                    if (existingUser == null) {
                                        userDao.insertUser(new User(
                                                UUID.randomUUID().toString(),
                                                "staffUser",
                                                password,
                                                "",
                                                "",
                                                email,
                                                "",
                                                "staff"
                                        ));
                                    } else {
                                        existingUser.password = password;
                                        existingUser.usertype = "staff";
                                        userDao.updateUser(existingUser);
                                    }
                                }).start();

                                Toast.makeText(StaffLoginActivity.this, "Registered new staff via API: " + email, Toast.LENGTH_SHORT).show();
                                saveLoginEmail(email);
                                startActivity(new Intent(StaffLoginActivity.this, StaffMainActivity.class));
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(StaffLoginActivity.this, "Register failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void saveLoginEmail(String email) {
        getSharedPreferences("login", MODE_PRIVATE)
                .edit()
                .putString("email", email)
                .apply();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }
}