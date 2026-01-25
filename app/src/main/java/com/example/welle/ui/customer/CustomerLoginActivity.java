package com.example.welle.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import com.example.welle.CustomerLoginedActivity;
import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.api.ApiClient;
import com.example.welle.api.ApiService;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.User;
import com.example.welle.data.local.UserDao;
import com.example.welle.data.remote.UserResponse;
import com.example.welle.data.remote.UserListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerLoginActivity extends AppCompatActivity {

    private Button btnBack;
    private Button btnCustomerOk;
    private EditText editEmail;

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

        // Back button → return to MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerLoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // OK button → check login via API or Room
        btnCustomerOk.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isNetworkAvailable(this)) {
                // Online → call API
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<UserListResponse> call = apiService.getAllUsers("student_123"); // 替換成你的 student_id

                call.enqueue(new Callback<UserListResponse>() {
                    @Override
                    public void onResponse(Call<UserListResponse> call, Response<UserListResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            boolean found = false;
                            for (UserResponse user : response.body().getUsers()) {
                                if (user.getEmail().equalsIgnoreCase(email)) {
                                    found = true;
                                    break;
                                }
                            }
                            if (found) {
                                Toast.makeText(CustomerLoginActivity.this, "Login success via API", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                            } else {
                                // 沒有找到 → 註冊新使用者
                                UserResponse newUser = new UserResponse();
                                newUser.setUsername(email);
                                newUser.setPassword("default");
                                newUser.setFirstname("New");
                                newUser.setLastname("Customer");
                                newUser.setEmail(email);
                                newUser.setContact("0000000000");
                                newUser.setUsertype("customer");

                                Call<Void> createCall = apiService.createUser("student_123", newUser);
                                createCall.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(CustomerLoginActivity.this, "Registered new user", Toast.LENGTH_SHORT).show();

                                            // 存入 Room
                                            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                                                    AppDatabase.class, "welle-db").allowMainThreadQueries().build();
                                            UserDao userDao = db.userDao();
                                            User localUser = new User("U" + System.currentTimeMillis(), email, "default",
                                                    "New", "Customer", email, "0000000000", "customer");
                                            userDao.insertUser(localUser);

                                            startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                                        } else {
                                            Toast.makeText(CustomerLoginActivity.this, "Register failed via API", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(CustomerLoginActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserListResponse> call, Throwable t) {
                        Toast.makeText(CustomerLoginActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // Offline → check Room database
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "welle-db").allowMainThreadQueries().build();

                UserDao userDao = db.userDao();
                User user = userDao.findUserByEmail(email);

                if (user != null) {
                    Toast.makeText(this, "Login success via local Room", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                } else {
                    Toast.makeText(this, "No local record found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Utility method: check network availability
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }
}