package com.example.welle.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.api.ApiClient;
import com.example.welle.api.ApiService;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.UserDao;
import com.example.welle.data.repository.UserRepository;
import com.example.welle.data.local.User;
import com.example.welle.data.remote.UserResponse;

import java.util.UUID;

public class CustomerLoginActivity extends AppCompatActivity {

    private Button btnBack;
    private Button btnCustomerOk;
    private EditText editEmail;

    private UserRepository userRepository;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_login);

        // Handle system bars safely
        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize UI components
        btnBack = findViewById(R.id.btnback);
        btnCustomerOk = findViewById(R.id.btncustomerok);
        editEmail = findViewById(R.id.editEmail);

        // Initialize Repository & DB
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        userDao = db.userDao();
        userRepository = new UserRepository(apiService, userDao);

        // Back button → return to MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerLoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // OK button → login or register
        btnCustomerOk.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isNetworkAvailable()) {
                // Offline → check local DB or register new user
                new Thread(() -> {
                    User localUser = userDao.findUserByEmail(email);
                    runOnUiThread(() -> {
                        if (localUser != null) {
                            Toast.makeText(this, "Offline login success: " + localUser.getEmail(), Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                        } else {
                            // Offline register new user
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
                            Toast.makeText(this, "Offline registered new user: " + newUser.getEmail(), Toast.LENGTH_SHORT).show();
                            saveLoginEmail(email);
                            startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                        }
                    });
                }).start();
                return;
            }

            // Online → check API
            userRepository.getUserByEmail("student_123", email, new UserRepository.RepositoryCallback<User>() {
                @Override
                public void onSuccess(User result) {
                    // Found user → save to local DB
                    new Thread(() -> userDao.insertUser(result)).start();
                    Toast.makeText(CustomerLoginActivity.this, "Login success: " + result.getEmail(), Toast.LENGTH_SHORT).show();
                    saveLoginEmail(email);
                    startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                }

                @Override
                public void onFailure(String errorMessage) {
                    // API not found → register new user
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
                            Toast.makeText(CustomerLoginActivity.this, "Registered new user: " + email, Toast.LENGTH_SHORT).show();
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
        });
    }

    // Save login email to SharedPreferences
    private void saveLoginEmail(String email) {
        getSharedPreferences("login", MODE_PRIVATE)
                .edit()
                .putString("email", email)
                .apply();
    }

    // Network check
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }
}