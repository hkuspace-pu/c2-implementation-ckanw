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

public class CustomerBookActivity extends AppCompatActivity{

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

        Button btn = findViewById(R.id.btnback);
        Button btn3 = findViewById(R.id.btntime);
        Button btn4 = findViewById(R.id.btncustomernotice);
        Button popupButton = findViewById(R.id.btncustomerfullmenu);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(CustomerBookActivity.this, CustomerMainActivity.class);
                startActivity(intent);
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(CustomerBookActivity.this, CustomerBookDetailActivity.class);
                startActivity(intent);
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(CustomerBookActivity.this, CustomerNoticeActivity.class);
                startActivity(intent);
            }
        });

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CustomerBookActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();

                inflater.inflate(R.menu.customermenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.customermainch) {
                            Intent intent = new Intent(CustomerBookActivity.this, CustomerMainActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customerbookch) {
                            Intent intent = new Intent(CustomerBookActivity.this, CustomerBookActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customermenuch) {
                            Intent intent = new Intent(CustomerBookActivity.this, CustomerMenuActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customernoticeuch) {
                            Intent intent = new Intent(CustomerBookActivity.this, CustomerNoticeActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customerpreferencech) {
                            Intent intent = new Intent(CustomerBookActivity.this, CustomerPrefActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customerhelpch) {
                            Toast.makeText(CustomerBookActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.customersignoutch) {
                            Intent intent = new Intent(CustomerBookActivity.this, MainActivity.class);
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
