package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.CustomerMenuActivity;
import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.User;
import com.example.welle.data.local.Booking;
import com.example.welle.utils.BookingUtils;

import java.util.List;

public class CustomerBookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_book_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 接收日期和時間
        String date = getIntent().getStringExtra("selectedDate");
        String time = getIntent().getStringExtra("selectedTime");

        TextView txtDate = findViewById(R.id.txtDate);
        TextView txtTime = findViewById(R.id.txtTime);
        TextView txtName = findViewById(R.id.editCustomerName);
        TextView txtEmail = findViewById(R.id.editCustomerEmail);
        TextView txtTel = findViewById(R.id.editCustomerContact);
        TextView editPerson = findViewById(R.id.editnoOfPerson);
        TextView editRemark = findViewById(R.id.editRemark);
        CheckBox checkBirthday = findViewById(R.id.checkBirthday);
        CheckBox checkFriends = findViewById(R.id.checkFriends);
        CheckBox checkBusiness = findViewById(R.id.checkBusiness);
        CheckBox checkOther = findViewById(R.id.checkOther);

        Button btnBack = findViewById(R.id.btnback);
        Button btnConfirm = findViewById(R.id.btnUpdate);
        Button btnNotice = findViewById(R.id.btncustomernotice);
        Button popupButton = findViewById(R.id.btncustomerfullmenu);

        if (date != null) txtDate.setText(date);
        if (time != null) txtTime.setText(time);

        // 從 SharedPreferences 取出登入的 email
        String loginEmail = getSharedPreferences("login", MODE_PRIVATE)
                .getString("email", null);

        if (loginEmail != null) {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            User user = db.userDao().findUserByEmail(loginEmail);

            if (user != null) {
                txtName.setText(user.firstname + user.lastname);
                txtEmail.setText(user.email);
                txtTel.setText(user.contact);
            }
        }

        // Back → 返回 CustomerBookActivity
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerBookDetailActivity.this, CustomerBookActivity.class);
            startActivity(intent);
        });

        // Confirm → 檢查是否有位子，再跳到 CustomerConfirmActivity
        btnConfirm.setOnClickListener(v -> {
            int noOfPerson;
            try {
                noOfPerson = Integer.parseInt(editPerson.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid number of persons", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder eventType = new StringBuilder();
            if (checkBirthday.isChecked()) eventType.append("Birthday ");
            if (checkFriends.isChecked()) eventType.append("Friends gathering ");
            if (checkBusiness.isChecked()) eventType.append("Business dinner ");
            if (checkOther.isChecked()) eventType.append("Other ");

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Booking> bookings = db.bookingDao().getBookingsByDate(date);

            boolean enough = BookingUtils.checkAvailability(bookings, date, time, noOfPerson);

            if (enough) {
                // ✅ 有位 → 跳到確認頁
                Intent intent = new Intent(CustomerBookDetailActivity.this, CustomerConfirmActivity.class);
                intent.putExtra("selectedDate", date);
                intent.putExtra("selectedTime", time);
                intent.putExtra("userName", txtName.getText().toString());
                intent.putExtra("userEmail", txtEmail.getText().toString());
                intent.putExtra("userTel", txtTel.getText().toString());
                intent.putExtra("noOfPerson", noOfPerson);
                intent.putExtra("remark", editRemark.getText().toString());
                intent.putExtra("eventType", eventType.toString().trim());
                startActivity(intent);
            } else {
                // ❌ 沒位 → 提示客人
                Toast.makeText(this, "該時段已滿，請選其他時間", Toast.LENGTH_SHORT).show();
            }
        });

        // Notice → 跳到 CustomerNoticeActivity
        btnNotice.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerBookDetailActivity.this, CustomerNoticeActivity.class);
            startActivity(intent);
        });

        // Popup menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CustomerBookDetailActivity.this, v);
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