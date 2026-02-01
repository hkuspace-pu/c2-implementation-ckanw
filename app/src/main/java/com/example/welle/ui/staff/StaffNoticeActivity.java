package com.example.welle.ui.staff;

import android.app.DatePickerDialog;
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
import com.example.welle.ui.staff.adapter.StaffNoticeAdapter;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Booking;
import com.example.welle.ui.MainActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StaffNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffNoticeAdapter adapter;
    private String selectedDate; // current selected date

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
        Button btnPickDate = findViewById(R.id.btnPickDate);

        recyclerView = findViewById(R.id.StaffRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        adapter = new StaffNoticeAdapter(this, null);
        recyclerView.setAdapter(adapter);

        // Default: load today's bookings
        Calendar calendar = Calendar.getInstance();
        selectedDate = String.format(Locale.getDefault(), "%04d-%d-%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        loadData(selectedDate);

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

        // Date picker button → choose any date
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    StaffNoticeActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        loadData(selectedDate);
                    },
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });
    }

    private void loadData(String date) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Booking> bookings = db.bookingDao().getBookingsByDate(date);

            runOnUiThread(() -> adapter.updateData(bookings));
        }).start();
    }
}