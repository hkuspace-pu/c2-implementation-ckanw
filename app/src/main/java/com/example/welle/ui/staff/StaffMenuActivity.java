package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.StaffNoticeActivity;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Menu;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.adapter.FoodItemAdapter;
import com.example.welle.ui.staff.adapter.FoodTypeAdapter;

import java.util.Arrays;
import java.util.List;

public class StaffMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerType;
    private RecyclerView recyclerItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ====== 原有的按鈕 ======
        Button btn = findViewById(R.id.btnback);
        Button btn3 = findViewById(R.id.btnstaffnotice);
        Button btn4 = findViewById(R.id.btnstaffadd);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMenuActivity.this, StaffMainActivity.class);
            startActivity(intent);
        });



        btn3.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMenuActivity.this, StaffNoticeActivity.class);
            startActivity(intent);
        });

        btn4.setOnClickListener(v -> {
            Intent intent = new Intent(StaffMenuActivity.this, StaffMenuAddActivity.class);
            startActivity(intent);
        });

        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffMenuActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.staffmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.staffmainch) {
                    startActivity(new Intent(StaffMenuActivity.this, StaffMainActivity.class));
                } else if (item.getItemId() == R.id.staffnoticech) {
                    startActivity(new Intent(StaffMenuActivity.this, StaffNoticeActivity.class));
                } else if (item.getItemId() == R.id.staffbookch) {
                    startActivity(new Intent(StaffMenuActivity.this, StaffBookActivity.class));
                } else if (item.getItemId() == R.id.staffmenuch) {
                    startActivity(new Intent(StaffMenuActivity.this, StaffMenuActivity.class));
                } else if (item.getItemId() == R.id.staffhelpch) {
                    Toast.makeText(StaffMenuActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.staffsignoutch) {
                    startActivity(new Intent(StaffMenuActivity.this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });

        // ====== 新增的 RecyclerView 顯示邏輯 ======
        recyclerType = findViewById(R.id.recyclerFoodType);
        recyclerItem = findViewById(R.id.recyclerFoodItem);

        recyclerType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerItem.setLayoutManager(new LinearLayoutManager(this));

        // 類型清單
        List<String> typeList = Arrays.asList("Lunch", "Dinner", "A la carte", "Desserts","Drinks");

        FoodTypeAdapter typeAdapter = new FoodTypeAdapter(typeList, type -> {
            // 點選類型 → 查 DB → 更新 itemAdapter
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                List<Menu> items = db.menuDao().getMenuByCategory(type);

                runOnUiThread(() -> {
                    recyclerItem.setAdapter(new FoodItemAdapter(items, menu -> {
                        // 點擊某個菜單 → 跳到編輯頁面
                        Intent intent = new Intent(StaffMenuActivity.this, StaffMenuDetailActivity.class);
                        intent.putExtra("menuId", menu.id); // 傳遞 ID
                        intent.putExtra("setName", menu.name);
                        intent.putExtra("setPrice", menu.price);
                        intent.putExtra("setType",menu.type);
                        startActivity(intent);
                    }));


                });
            }).start();
        });

        recyclerType.setAdapter(typeAdapter);

        // 預設顯示 Dinner
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Menu> items = db.menuDao().getMenuByCategory("Dinner");

            runOnUiThread(() -> {
                recyclerItem.setAdapter(new FoodItemAdapter(items, menu -> {
                    // 點擊某個菜單 → 跳到編輯頁面
                    Intent intent = new Intent(StaffMenuActivity.this, StaffMenuDetailActivity.class);
                    intent.putExtra("menuId", menu.id); // 傳遞 ID
                    intent.putExtra("setName", menu.name);
                    intent.putExtra("setPrice", menu.price);
                    intent.putExtra("setType",menu.type);
                    startActivity(intent);
                }));


            });
        }).start();
    }
}