package com.example.welle.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.R;
import com.example.welle.ui.staff.StaffLoginActivity;
import com.example.welle.ui.customer.CustomerLoginActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCustomer;
    private Button btnStaff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Handle system bars (status bar, navigation bar) safely
        View rootView = findViewById(R.id.main);
        if (rootView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Initialize buttons
        btnCustomer = findViewById(R.id.btnCustomer);
        btnStaff = findViewById(R.id.btnStaff);

        // Customer button click event → go to CustomerLoginActivity
        btnCustomer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CustomerLoginActivity.class);
            startActivity(intent);
        });

        // Staff button click event → go to StaffLoginActivity
        btnStaff.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StaffLoginActivity.class);
            startActivity(intent);
        });
    }
}
