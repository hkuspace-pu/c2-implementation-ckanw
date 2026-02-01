package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.StaffNoticeAdapter;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Booking;
import com.example.welle.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StaffNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffNoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_notice);

        // Handle system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnback);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        recyclerView = findViewById(R.id.StaffRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new StaffNoticeAdapter(this, null);
        recyclerView.setAdapter(adapter);

        // Load booking data from DB
        loadData();

        // Back button → return to StaffMainActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(StaffNoticeActivity.this, StaffMainActivity.class);
            startActivity(intent);
        });

        // Popup menu navigation
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffNoticeActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.staffmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.staffmainch) {
                    startActivity(new Intent(StaffNoticeActivity.this, StaffMainActivity.class));
                } else if (item.getItemId() == R.id.staffnoticech) {
                    startActivity(new Intent(StaffNoticeActivity.this, StaffNoticeActivity.class));
                } else if (item.getItemId() == R.id.staffbookch) {
                    startActivity(new Intent(StaffNoticeActivity.this, StaffBookActivity.class));
                } else if (item.getItemId() == R.id.staffmenuch) {
                    startActivity(new Intent(StaffNoticeActivity.this, StaffMenuActivity.class));
                } else if (item.getItemId() == R.id.staffhelpch) {
                    Toast.makeText(StaffNoticeActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.staffsignoutch) {
                    startActivity(new Intent(StaffNoticeActivity.this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });
    }

    private void loadData() {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());

            // Get today's date string
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(new Date());

            // Query bookings from today onwards
            List<Booking> bookings = db.bookingDao().getUpcomingBookings(today);

            runOnUiThread(() -> adapter.updateData(bookings));
        }).start();
    }
}