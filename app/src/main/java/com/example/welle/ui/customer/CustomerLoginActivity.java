package com.example.welle.ui.customer;

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

public class CustomerLoginActivity extends AppCompatActivity {

    private Button btnBack, btnCustomerOk;
    private EditText editEmail;
    private UserDao userDao;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_login);

        btnBack = findViewById(R.id.btnback);
        btnCustomerOk = findViewById(R.id.btncustomerok);
        editEmail = findViewById(R.id.editEmail);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();
        userRepository = new UserRepository(apiService, userDao);

        btnBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        btnCustomerOk.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim().toLowerCase();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNetworkAvailable()) {
                // 🔹 Offline
                new Thread(() -> {
                    User localUser = userDao.findUserByEmailAndType(email, "customer");
                    runOnUiThread(() -> {
                        if (localUser != null) {
                            Toast.makeText(this, "Offline login success: " + localUser.getEmail(), Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(this, CustomerLoginedActivity.class));
                        } else {
                            // 離線新增 customer
                            User newUser = new User(
                                    UUID.randomUUID().toString(),
                                    "offlineUser",
                                    "",
                                    "",
                                    "",
                                    email,
                                    "",
                                    "customer"
                            );
                            new Thread(() -> userDao.insertUser(newUser)).start();
                            Toast.makeText(this, "Offline registered new customer: " + email, Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(this, CustomerLoginedActivity.class));
                        }
                    });
                }).start();
            } else {
                // 🔹 Online
                userRepository.getUserByEmail("student_123", email, new UserRepository.RepositoryCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        new Thread(() -> {
                            User existingUser = userDao.findUserByEmailAndType(email, "customer");
                            if (existingUser == null) {
                                userDao.insertUser(result);
                            } else {
                                existingUser.firstname = result.firstname;
                                existingUser.lastname = result.lastname;
                                existingUser.contact = result.contact;
                                userDao.updateUser(existingUser);
                            }
                        }).start();

                        Toast.makeText(CustomerLoginActivity.this, "Login success: " + result.getEmail(), Toast.LENGTH_SHORT).show();
                        saveLoginEmail(email);
                        startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        UserResponse newUserResponse = new UserResponse();
                        newUserResponse.setUsername("newUser");
                        newUserResponse.setPassword("");
                        newUserResponse.setFirstname("");
                        newUserResponse.setLastname("");
                        newUserResponse.setEmail(email);
                        newUserResponse.setContact("");
                        newUserResponse.setUsertype("customer");

                        userRepository.registerUser("student_123", newUserResponse, new UserRepository.RepositoryCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean result) {
                                User newUser = new User(
                                        UUID.randomUUID().toString(),
                                        "offlineUser",
                                        "",
                                        "",
                                        "",
                                        email,
                                        "",
                                        "customer"
                                );
                                new Thread(() -> userDao.insertUser(newUser)).start();
                                Toast.makeText(CustomerLoginActivity.this, "Registered new customer: " + email, Toast.LENGTH_SHORT).show();
                                saveLoginEmail(email);
                                startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(CustomerLoginActivity.this, "Register failed: " + errorMessage, Toast.LENGTH_SHORT).show();
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