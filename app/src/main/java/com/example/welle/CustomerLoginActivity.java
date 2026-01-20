package com.example.welle;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerLoginActivity extends AppCompatActivity {

    private Button btnBack;
    private Button btnCustomerOk;
    private EditText editEmail;
    private EditText editMobile;

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
        editMobile = findViewById(R.id.editMobile);

        // Back button → return to MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerLoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // OK button → check login via API or Room
        btnCustomerOk.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String mobile = editMobile.getText().toString().trim();

            if (email.isEmpty() || mobile.isEmpty()) {
                Toast.makeText(this, "Please enter both Email and Mobile", Toast.LENGTH_SHORT).show();
                return;
            }

            if (isNetworkAvailable(this)) {
                // Online → call API
                ApiService apiService = ApiClient.getClient().create(ApiService.class);
                Call<UserResponse> call = apiService.loginUser(email, mobile);

                call.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(CustomerLoginActivity.this, "Login success via API", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                        } else {
                            Toast.makeText(CustomerLoginActivity.this, "Login failed via API", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(CustomerLoginActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                // Offline → check Room database
                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "welle-db").allowMainThreadQueries().build();

                UserDao userDao = db.userDao();
                User user = userDao.findUserByEmailAndMobile(email, mobile);

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


