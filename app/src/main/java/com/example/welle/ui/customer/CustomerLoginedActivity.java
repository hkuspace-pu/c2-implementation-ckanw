package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.R;

public class CustomerLoginedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_logined);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnback);
        Button btnOk = findViewById(R.id.btncustomerok);
        Button btnResend = findViewById(R.id.btncustomerresend);

        // Back button → return to previous screen
        btnBack.setOnClickListener(v -> finish());

        // OK button → go to CustomerMainActivity
        btnOk.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomerMainActivity.class);
            startActivity(intent);
        });

        // Resend button → show message
        btnResend.setOnClickListener(v ->
                Toast.makeText(this, "OTP has been resent!", Toast.LENGTH_SHORT).show()
        );
    }
}
