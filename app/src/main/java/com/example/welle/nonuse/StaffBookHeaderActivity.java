package com.example.welle.nonuse;

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

import com.example.welle.R;
import com.example.welle.StaffNoticeActivity;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.StaffBookActivity;
import com.example.welle.ui.staff.StaffBookDetailActivity;
import com.example.welle.ui.staff.StaffMainActivity;
import com.example.welle.ui.staff.StaffMenuActivity;

public class StaffBookHeaderActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_book_header);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.btnback);
        Button btn2 = findViewById(R.id.btnstaffconfirmdate);
        Button btn3 = findViewById(R.id.btnstaffnotice);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffBookHeaderActivity.this, StaffBookActivity.class);
                startActivity(intent);
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffBookHeaderActivity.this, StaffBookDetailActivity.class);
                startActivity(intent);
            }
        });



        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffBookHeaderActivity.this, StaffNoticeActivity.class);
                startActivity(intent);
            }
        });

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(StaffBookHeaderActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();

                inflater.inflate(R.menu.staffmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.staffmainch) {
                            Intent intent = new Intent(StaffBookHeaderActivity.this, StaffMainActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffnoticech) {
                            Intent intent = new Intent(StaffBookHeaderActivity.this, StaffNoticeActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffbookch) {
                            Intent intent = new Intent(StaffBookHeaderActivity.this, StaffBookActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffmenuch) {
                            Intent intent = new Intent(StaffBookHeaderActivity.this, StaffMenuActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffhelpch) {
                            Toast.makeText(StaffBookHeaderActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.staffsignoutch) {
                            Intent intent = new Intent(StaffBookHeaderActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });










    }
}