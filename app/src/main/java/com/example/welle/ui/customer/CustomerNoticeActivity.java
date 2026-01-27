package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
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

import com.example.welle.CustomerMenuActivity;
import com.example.welle.R;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Booking;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.customer.adapter.CustomerNoticeAdapter;

import java.util.List;

public class CustomerNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomerNoticeAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_notice);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.CustomerNoticeRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        db = AppDatabase.getInstance(getApplicationContext());

        loadBookings();

        // Back 按鈕
        Button btn = findViewById(R.id.btnback);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerNoticeActivity.this, CustomerMainActivity.class);
            startActivity(intent);
        });

        // Popup menu
        Button popupButton = findViewById(R.id.btncustomerfullmenu);
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CustomerNoticeActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.customermenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.customermainch) {
                    startActivity(new Intent(this, CustomerMainActivity.class));
                } else if (item.getItemId() == R.id.customerbookch) {
                    startActivity(new Intent(this, CustomerBookActivity.class));
                } else if (item.getItemId() == R.id.customermenuch) {
                    startActivity(new Intent(this, CustomerMenuActivity.class));
                } else if (item.getItemId() == R.id.customernoticeuch) {
                    startActivity(new Intent(this, CustomerNoticeActivity.class));
                } else if (item.getItemId() == R.id.customerpreferencech) {
                    startActivity(new Intent(this, CustomerPrefActivity.class));
                } else if (item.getItemId() == R.id.customerhelpch) {
                    Toast.makeText(this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.customersignoutch) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });
    }

    private void loadBookings() {
        String loginEmail = getSharedPreferences("login", MODE_PRIVATE)
                .getString("email", null);

        new Thread(() -> {
            List<Booking> bookings;
            if (loginEmail != null) {
                bookings = db.bookingDao().getBookingsByEmail(loginEmail);
            } else {
                bookings = db.bookingDao().getAllBookings();
            }

            runOnUiThread(() -> {
                adapter = new CustomerNoticeAdapter(bookings);
                recyclerView.setAdapter(adapter);

                // ✅ 在這裡設定 listener，確保 adapter 已經建立
                adapter.setOnItemClickListener(booking -> {
                    Intent intent = new Intent(this, CustomerBookEditActivity.class);
                    intent.putExtra("bookingId", booking.id);
                    startActivityForResult(intent, 100);
                });
            });
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            boolean updated = data.getBooleanExtra("updated", false);
            boolean deleted = data.getBooleanExtra("deleted", false);
            if (updated || deleted) {
                loadBookings(); // 重新載入列表
            }
        }
    }
}