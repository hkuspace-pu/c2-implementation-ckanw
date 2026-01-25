package com.example.welle.ui.customer;

import android.content.Intent;
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

import com.example.welle.CustomerLoginedActivity;
import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.api.ApiClient;
import com.example.welle.api.ApiService;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.UserDao;
import com.example.welle.data.repository.UserRepository;
import com.example.welle.data.local.User;

public class CustomerLoginActivity extends AppCompatActivity {

    private Button btnBack;
    private Button btnCustomerOk;
    private EditText editEmail;

    private UserRepository userRepository;

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

        // 初始化 Repository
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        UserDao userDao = db.userDao();
        userRepository = new UserRepository(apiService, userDao);

        // Back button → return to MainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerLoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // OK button → check login via Repository
        btnCustomerOk.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            userRepository.getUserByEmail("student_123", email, new UserRepository.RepositoryCallback<User>() {
                @Override
                public void onSuccess(User result) {
                    Toast.makeText(CustomerLoginActivity.this, "Login success: " + result.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CustomerLoginActivity.this, CustomerLoginedActivity.class));
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(CustomerLoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}