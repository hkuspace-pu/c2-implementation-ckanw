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
import com.example.welle.ui.staff.StaffBookActivity;
import com.example.welle.ui.staff.StaffMainActivity;

import java.util.ArrayList;
import java.util.List;

public class StaffNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StaffNoticeAdapter adapter;
    private List<staffItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_notice);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Button btn = findViewById(R.id.btnback);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        recyclerView = findViewById(R.id.StaffRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        itemList = new ArrayList<>();
        loadData();
        adapter = new StaffNoticeAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Explicit Intent
                //goToSecondActivity();
                Intent intent = new Intent(StaffNoticeActivity.this, StaffMainActivity.class);
                startActivity(intent);
            }
        });

        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(StaffNoticeActivity.this, v);

                MenuInflater inflater = popup.getMenuInflater();

                inflater.inflate(R.menu.staffmenu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.staffmainch) {
                            Intent intent = new Intent(StaffNoticeActivity.this, StaffMainActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffnoticech) {
                            Intent intent = new Intent(StaffNoticeActivity.this, StaffNoticeActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffbookch) {
                            Intent intent = new Intent(StaffNoticeActivity.this, StaffBookActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffmenuch) {
                            Intent intent = new Intent(StaffNoticeActivity.this, StaffMenuActivity.class);
                            startActivity(intent);
                        } else if (item.getItemId() == R.id.staffhelpch) {
                            Toast.makeText(StaffNoticeActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.staffsignoutch) {
                            Intent intent = new Intent(StaffNoticeActivity.this, MainActivity.class);
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
        itemList.add(new staffItem("12:00", "Miss Cheung - She boss will put the wine to shop for tonight"));
        itemList.add(new staffItem("13:00", "Mr Ko - Brithday"));
        itemList.add(new staffItem("13:00", "Vivian Chiu - Friend Gathering"));
        itemList.add(new staffItem("18:00", "Sammi Hui - Friend Gathering"));
        itemList.add(new staffItem( "19:00", "Mrs. Lee - Company Party"));
        itemList.add(new staffItem( "20:30", "Jacky Cheung - Anniversary"));
    }

}