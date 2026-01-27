package com.example.welle;

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

import com.example.welle.ui.MainActivity;

public class StaffBookDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_book_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.btnstaffconfirmbk);
        Button btn2 = findViewById(R.id.btnback);
        Button btn3 = findViewById(R.id.btnstaffnotice);
        Button btn4 = findViewById(R.id.btnDelete);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Toast.makeText(StaffBookDetailActivity.this, "Record is update", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffBookDetailActivity.this, StaffBookActivity.class);
                startActivity(intent);
            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffBookDetailActivity.this, StaffBookHeaderActivity.class);
                startActivity(intent);
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffBookDetailActivity.this, StaffNoticeActivity.class);
                startActivity(intent);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Toast.makeText(StaffBookDetailActivity.this, "Record is Canceled!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StaffBookDetailActivity.this, StaffBookActivity.class);
                startActivity(intent);
            }
        });

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(StaffBookDetailActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();

                inflater.inflate(R.menu.staffmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.staffmainch) {
                            Intent intent = new Intent(StaffBookDetailActivity.this, StaffMainActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffnoticech) {
                            Intent intent = new Intent(StaffBookDetailActivity.this, StaffNoticeActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffbookch) {
                            Intent intent = new Intent(StaffBookDetailActivity.this, StaffBookActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffmenuch) {
                            Intent intent = new Intent(StaffBookDetailActivity.this, StaffMenuActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffhelpch) {
                            Toast.makeText(StaffBookDetailActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.staffsignoutch) {
                            Intent intent = new Intent(StaffBookDetailActivity.this, MainActivity.class);
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
