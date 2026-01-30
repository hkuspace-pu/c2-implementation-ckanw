package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.welle.R;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Booking;

public class CustomerBookEditActivity extends AppCompatActivity {

    private AppDatabase db;
    private Booking booking;

    private EditText editDate, editTime, editName, editEmail, editTel, editPerson, editRemark;
    private Button btnUpdate, btnDelete, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_book_edit);

        db = AppDatabase.getInstance(getApplicationContext());
        int bookingId = getIntent().getIntExtra("bookingId", -1);

        editDate = findViewById(R.id.txtDate);
        editTime = findViewById(R.id.txtTime);
        editName = findViewById(R.id.editCustomerName);
        editEmail = findViewById(R.id.editCustomerEmail);
        editTel = findViewById(R.id.editCustomerContact);
        editPerson = findViewById(R.id.editnoOfPerson);
        editRemark = findViewById(R.id.editCustomerRemark);

        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnback);

        new Thread(() -> {
            booking = db.bookingDao().getBookingById(bookingId);
            runOnUiThread(() -> {
                if (booking != null) {
                    editDate.setText(booking.date);
                    editTime.setText(booking.time);
                    editName.setText(booking.name);
                    editEmail.setText(booking.email);
                    editTel.setText(booking.tel);
                    editPerson.setText(String.valueOf(booking.noOfPerson));
                    editRemark.setText(booking.remark);
                }
            });
        }).start();

        btnUpdate.setOnClickListener(v -> {
            if (booking != null) {
                booking.date = editDate.getText().toString();
                booking.time = editTime.getText().toString();
                booking.name = editName.getText().toString();
                booking.email = editEmail.getText().toString();
                booking.tel = editTel.getText().toString();
                booking.noOfPerson = Integer.parseInt(editPerson.getText().toString());
                booking.remark = editRemark.getText().toString();

                new Thread(() -> db.bookingDao().updateBooking(booking)).start();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("updated", true);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Booking updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (booking != null) {
                new Thread(() -> db.bookingDao().deleteBooking(booking)).start();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("deleted", true);
                setResult(RESULT_OK, resultIntent);
                Toast.makeText(this, "Booking cancelled", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}