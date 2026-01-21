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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomerNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<staffItem> itemList;

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

        // Initialize data and adapter
        itemList = new ArrayList<>();
        loadData();
        adapter = new ItemAdapter(this, itemList);

        recyclerView.setAdapter(adapter);





        Button btn = findViewById(R.id.btnback);
        Button btn2 = findViewById(R.id.button8);

        Button popupButton = findViewById(R.id.btncustomerfullmenu);

        // Initialize RecyclerView




        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(CustomerNoticeActivity.this, CustomerMainActivity.class);
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(CustomerNoticeActivity.this, CustomerBookEditActivity.class);
                startActivity(intent);
            }
        });

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(CustomerNoticeActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();

                inflater.inflate(R.menu.customermenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.customermainch) {
                            Intent intent = new Intent(CustomerNoticeActivity.this, CustomerMainActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customerbookch) {
                            Intent intent = new Intent(CustomerNoticeActivity.this, CustomerBookActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customermenuch) {
                            Intent intent = new Intent(CustomerNoticeActivity.this, CustomerMenuActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customernoticeuch) {
                            Intent intent = new Intent(CustomerNoticeActivity.this, CustomerNoticeActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customerpreferencech) {
                            Intent intent = new Intent(CustomerNoticeActivity.this, CustomerPrefActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.customerhelpch) {
                            Toast.makeText(CustomerNoticeActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.customersignoutch) {
                            Intent intent = new Intent(CustomerNoticeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });





    }
    private void loadData() {
        // Sample data for testing
        itemList.add(new staffItem( "2025-02-14", "Valentine's Day"));
        itemList.add(new staffItem( "2025-07-14", "Chinese Ghost Festival"));
        itemList.add(new staffItem( "2025-07-16", "Birthday"));
        itemList.add(new staffItem( "2025-10-31", "Halloween"));
        itemList.add(new staffItem( "2025-12-14", "Have a get-together"));
    }

}