package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.CustomerMenuActivity;
import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Booking;

public class CustomerConfirmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_confirm);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 🔹 接收日期與時間
        String date = getIntent().getStringExtra("selectedDate");
        String time = getIntent().getStringExtra("selectedTime");
        String name = getIntent().getStringExtra("userName");
        String email = getIntent().getStringExtra("userEmail");
        String tel = getIntent().getStringExtra("userTel");
        int noOfPerson = getIntent().getIntExtra("noOfPerson",0);
        String remark = getIntent().getStringExtra("remark");
        String eventType = getIntent().getStringExtra("eventType");



        // Show data on screen
        TextView txtDate = findViewById(R.id.textView49);
        TextView txtTime = findViewById(R.id.textView50);

        if (date != null) txtDate.setText(date);
        if (time != null) txtTime.setText(time);


        Button btnOk = findViewById(R.id.btnok);
        Button btnBack = findViewById(R.id.btnback);
        Button btnNotice = findViewById(R.id.btncustomernotice);
        Button popupButton = findViewById(R.id.btncustomerfullmenu);

        // Back → 返回 CustomerBookDetailActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerConfirmActivity.this, CustomerBookDetailActivity.class);
            startActivity(intent);
        });

        // OK → 顯示提示並跳到主頁
        btnOk.setOnClickListener(v -> {

            // save booking only when user presses ok
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Booking booking = new Booking();
            booking.date = date;
            booking.time = time;
            booking.name = name;
            booking.email = email;
            booking.tel = tel;
            booking.noOfPerson=noOfPerson;
            booking.remark=remark;
            booking.eventType = eventType;

            new Thread(() -> db.bookingDao().insertBooking(booking)).start();


            Toast.makeText(this,
                    "Your booking is confirmed for " + date + " at " + time,
                    Toast.LENGTH_LONG).show();

            Intent intent = new Intent(CustomerConfirmActivity.this, CustomerMainActivity.class);
            startActivity(intent);
            finish();
        });

        // Notice → 跳到 CustomerNoticeActivity
        btnNotice.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerConfirmActivity.this, CustomerNoticeActivity.class);
            startActivity(intent);
        });

        // Popup menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CustomerConfirmActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.customermenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.customermainch) {
                    startActivity(new Intent(this, CustomerMainActivity.class));
                } else if (id == R.id.customerbookch) {
                    startActivity(new Intent(this, CustomerBookActivity.class));
                } else if (id == R.id.customermenuch) {
                    startActivity(new Intent(this, CustomerMenuActivity.class));
                } else if (id == R.id.customernoticeuch) {
                    startActivity(new Intent(this, CustomerNoticeActivity.class));
                } else if (id == R.id.customerpreferencech) {
                    startActivity(new Intent(this, CustomerPrefActivity.class));
                } else if (id == R.id.customerhelpch) {
                    Toast.makeText(this, "App version: v1.0", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.customersignoutch) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });
    }
}