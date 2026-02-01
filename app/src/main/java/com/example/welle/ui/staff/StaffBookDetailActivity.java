package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.view.MenuInflater;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.welle.R;
import com.example.welle.nonuse.StaffBookHeaderActivity;
import com.example.welle.ui.MainActivity;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Booking;

public class StaffBookDetailActivity extends AppCompatActivity {

    private EditText txtDate, txtTime;
    private EditText editNoOfPerson, editCustomerName, editCustomerContact, editRemark;
    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_book_detail);

        Button btnConfirm = findViewById(R.id.btnstaffconfirmbk);
        Button btnBack = findViewById(R.id.btnback);
        Button btnNotice = findViewById(R.id.btnstaffnotice);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        editNoOfPerson = findViewById(R.id.editnoOfPerson);
        editCustomerName = findViewById(R.id.editCustomerName);
        editCustomerContact = findViewById(R.id.editCustomerContact);
        editRemark = findViewById(R.id.editTextText7);

        // 確認按鈕 → 寫入 DB 並跳到選檯位頁面
        btnConfirm.setOnClickListener(v -> {
            String date = txtDate.getText().toString().trim();
            String time = txtTime.getText().toString().trim();
            int noOfPerson;
            try {
                noOfPerson = Integer.parseInt(editNoOfPerson.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number of person", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = editCustomerName.getText().toString().trim();
            String tel = editCustomerContact.getText().toString().trim();
            String remark = editRemark.getText().toString().trim();

            Booking booking = new Booking();
            booking.date = date;
            booking.time = time;
            booking.noOfPerson = noOfPerson;
            booking.name = name;
            booking.tel = tel;
            booking.remark = remark;
            booking.eventType = "一般訂位";
            booking.tableName = null;

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.bookingDao().insertBooking(booking);
                bookingId = booking.id; // Room 會自動生成 id
            }).start();

            Toast.makeText(this, "Record is inserted", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(StaffBookDetailActivity.this, StaffSelectTableActivity.class);
            intent.putExtra("selectedDate", date);
            intent.putExtra("selectedTime", time);
            intent.putExtra("bookingId", bookingId);
            startActivity(intent);
        });

        // 返回按鈕
        btnBack.setOnClickListener(v -> startActivity(new Intent(this, StaffBookHeaderActivity.class)));

        // 通知按鈕
        btnNotice.setOnClickListener(v -> startActivity(new Intent(this, StaffNoticeActivity.class)));

        // 刪除按鈕
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Record is canceled!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, StaffBookActivity.class));
        });

        // 選單按鈕
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffBookDetailActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.staffmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.staffmainch) {
                    startActivity(new Intent(this, StaffMainActivity.class));
                } else if (item.getItemId() == R.id.staffnoticech) {
                    startActivity(new Intent(this, StaffNoticeActivity.class));
                } else if (item.getItemId() == R.id.staffbookch) {
                    startActivity(new Intent(this, StaffBookActivity.class));
                } else if (item.getItemId() == R.id.staffmenuch) {
                    startActivity(new Intent(this, StaffMenuActivity.class));
                } else if (item.getItemId() == R.id.staffhelpch) {
                    Toast.makeText(this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.staffsignoutch) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });
    }
}