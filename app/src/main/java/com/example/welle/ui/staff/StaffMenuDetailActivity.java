package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Menu;
import com.example.welle.data.local.MenuDetail;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.adapter.FoodItemAdapter;
import com.example.welle.ui.staff.adapter.FoodTypeAdapter;
import com.example.welle.ui.staff.adapter.SelectedFoodAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StaffMenuDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerFoodType;
    private RecyclerView recyclerFoodOptions;
    private RecyclerView recyclerSelectedFoods;

    private List<Menu> selectedList = new ArrayList<>();
    private FoodItemAdapter optionsAdapter;
    private SelectedFoodAdapter selectedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_menu_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 接收前一頁傳來的資料
        String setName = getIntent().getStringExtra("setName");
        double setPrice = getIntent().getDoubleExtra("setPrice", 0.0);
        String setType = getIntent().getStringExtra("setType");
        int menuId = getIntent().getIntExtra("menuId", -1);

        // 顯示在 EditText
        EditText edtName = findViewById(R.id.editTextName);
        EditText edtPrice = findViewById(R.id.editTextPrice);
        edtName.setText(setName);
        edtPrice.setText(String.valueOf(setPrice));

        // ====== 按鈕 ======
        Button btnBack = findViewById(R.id.btnback);
        Button btnConfirm = findViewById(R.id.btnstaffconfirmmenu);
        Button btnCancel = findViewById(R.id.btnstaffcancelmenu);
        Button btnClear = findViewById(R.id.btnstaffclear2);
        Button btnReset = findViewById(R.id.btnstaffreset);
        Button btnNotice = findViewById(R.id.btnstaffnotice);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, StaffMenuActivity.class));
            finish();
        });

        btnNotice.setOnClickListener(v -> startActivity(new Intent(this, StaffNoticeActivity.class)));

        btnConfirm.setOnClickListener(v -> {
            if (menuId == -1) {
                Toast.makeText(this, "Menu ID 無效", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                for (Menu selected : selectedList) {
                    if (selected.quantity > 0) {
                        MenuDetail detail = new MenuDetail();
                        detail.menuId = menuId;
                        detail.foodName = selected.name;
                        detail.quantity = selected.quantity;
                        db.menuDetailDao().insert(detail);
                    }
                }
            }).start();

            Toast.makeText(this, "已新增 " + selectedList.size() + " 個細項到 Menu ID " + menuId, Toast.LENGTH_SHORT).show();

            // 🔹 跳回前一頁
            Intent intent = new Intent(this, StaffMenuActivity.class);
            startActivity(intent);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Record is No change!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, StaffMenuActivity.class));
            finish();
        });

        btnClear.setOnClickListener(v -> {
            selectedList.clear();
            selectedAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Record is Clear!", Toast.LENGTH_SHORT).show();
        });

        btnReset.setOnClickListener(v -> {
            loadMenuByCategory("A la carte");
            selectedList.clear();
            selectedAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Record is Reset!", Toast.LENGTH_SHORT).show();
        });

        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, v);
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

        // ====== RecyclerView 顯示邏輯 ======
        recyclerFoodType = findViewById(R.id.recyclerFoodType);
        recyclerFoodOptions = findViewById(R.id.recyclerFoodOptions);
        recyclerSelectedFoods = findViewById(R.id.recyclerSelectedFoods);

        recyclerFoodType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFoodOptions.setLayoutManager(new LinearLayoutManager(this));
        recyclerSelectedFoods.setLayoutManager(new LinearLayoutManager(this));

        List<String> typeList = Arrays.asList("A la carte", "Desserts", "Drinks");
        FoodTypeAdapter typeAdapter = new FoodTypeAdapter(typeList, type -> loadMenuByCategory(type));
        recyclerFoodType.setAdapter(typeAdapter);

        // 初始化已選清單 Adapter
        selectedAdapter = new SelectedFoodAdapter(selectedList, menu -> {
            int pos = selectedList.indexOf(menu);
            if (pos >= 0) {
                selectedList.remove(pos);
                selectedAdapter.notifyItemRemoved(pos);
            }
        });
        recyclerSelectedFoods.setAdapter(selectedAdapter);

        // 🔹 檢查 DB 是否已有套餐，載入到購物車
        if (menuId != -1) {
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                List<MenuDetail> details = db.menuDetailDao().getDetailsByMenuId(menuId);

                List<Menu> restoredList = new ArrayList<>();
                for (MenuDetail d : details) {
                    Menu m = new Menu();
                    m.id = d.menuId;
                    m.name = d.foodName;
                    m.quantity = d.quantity;
                    restoredList.add(m);
                }

                runOnUiThread(() -> {
                    selectedList.clear();
                    selectedList.addAll(restoredList);
                    selectedAdapter.notifyDataSetChanged();
                    if (!restoredList.isEmpty()) {
                        Toast.makeText(this, "已載入之前的套餐內容", Toast.LENGTH_SHORT).show();
                    }
                });
            }).start();
        }

        // 預設顯示 A la carte
        loadMenuByCategory("A la carte");
    }

    private void loadMenuByCategory(String category) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Menu> items = db.menuDao().getMenuByCategory(category);

            runOnUiThread(() -> {
                optionsAdapter = new FoodItemAdapter(items, menu -> {
                    selectedList.add(menu);
                    selectedAdapter.notifyItemInserted(selectedList.size() - 1);
                    Toast.makeText(this, menu.name + " added", Toast.LENGTH_SHORT).show();
                });
                recyclerFoodOptions.setAdapter(optionsAdapter);
            });
        }).start();
    }
}