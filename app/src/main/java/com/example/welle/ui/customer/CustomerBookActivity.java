package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CalendarView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.CustomerMenuActivity;
import com.example.welle.R;
import com.example.welle.ui.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomerBookActivity extends AppCompatActivity {

    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_book);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btnback);
        Button btnNotice = findViewById(R.id.btncustomernotice);
        Button popupButton = findViewById(R.id.btncustomerfullmenu);
        CalendarView calendarView = findViewById(R.id.calendarView);
        LinearLayout timeContainer = findViewById(R.id.timeContainer);

        // Back button → return to main
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerBookActivity.this, CustomerMainActivity.class);
            startActivity(intent);
        });

        // Notice button → go to notice page
        btnNotice.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerBookActivity.this, CustomerNoticeActivity.class);
            startActivity(intent);
        });

        // Popup menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CustomerBookActivity.this, v);
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

        // 🔹 監聽日期選擇
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        // 🔹 動態生成時間段 (17:00 → 21:30，每 15 分鐘)
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 0);

        Calendar end = Calendar.getInstance();
        end.set(Calendar.HOUR_OF_DAY, 21);
        end.set(Calendar.MINUTE, 30);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        while (calendar.before(end) || calendar.equals(end)) {
            TextView tv = new TextView(this);
            tv.setText(sdf.format(calendar.getTime()));
            tv.setTextSize(18);
            tv.setPadding(24, 16, 24, 16);
            tv.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

            // 點擊事件 → 跳到下一頁並傳遞日期+時間
            tv.setOnClickListener(v -> {
                if (!selectedDate.isEmpty()) {
                    Intent intent = new Intent(CustomerBookActivity.this, CustomerBookDetailActivity.class);
                    intent.putExtra("selectedDate", selectedDate);
                    intent.putExtra("selectedTime", tv.getText().toString());
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please select a date first", Toast.LENGTH_SHORT).show();
                }
            });

            timeContainer.addView(tv);
            calendar.add(Calendar.MINUTE, 15);
        }
    }
}


