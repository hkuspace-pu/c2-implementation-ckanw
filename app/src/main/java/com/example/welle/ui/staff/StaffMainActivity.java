package com.example.welle.ui.staff;

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

import com.example.welle.R;
import com.example.welle.StaffMenuActivity;
import com.example.welle.StaffNoticeActivity;
import com.example.welle.ui.MainActivity;

public class StaffMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // MENU / BOOKING 用 TextView
        TextView btnMenu = findViewById(R.id.textViewMenu);
        TextView btnBooking = findViewById(R.id.textViewBooking);

        // Notice 用 Button
        Button btnNotice = findViewById(R.id.btnstaffnotice);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        // MENU → StaffMenuActivity
        btnMenu.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, StaffMenuActivity.class);
            startActivity(intent);
        });

        // BOOKING → StaffBookActivity
        btnBooking.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, StaffBookActivity.class);
            startActivity(intent);
        });

        // Notice → StaffNoticeActivity
        btnNotice.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMainActivity.this, StaffNoticeActivity.class);
            startActivity(intent);
        });

        // Popup Menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffMainActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.staffmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.staffmainch) {
                    startActivity(new Intent(StaffMainActivity.this, StaffMainActivity.class));
                } else if (id == R.id.staffnoticech) {
                    startActivity(new Intent(StaffMainActivity.this, StaffNoticeActivity.class));
                } else if (id == R.id.staffbookch) {
                    startActivity(new Intent(StaffMainActivity.this, StaffBookActivity.class));
                } else if (id == R.id.staffmenuch) {
                    startActivity(new Intent(StaffMainActivity.this, StaffMenuActivity.class));
                } else if (id == R.id.staffhelpch) {
                    Toast.makeText(StaffMainActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.staffsignoutch) {
                    startActivity(new Intent(StaffMainActivity.this, MainActivity.class));
                }

                return true;
            });

            popup.show();
        });
    }
}