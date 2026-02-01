package com.example.welle.ui.customer;

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
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Menu;
import com.example.welle.data.local.MenuDetail;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.customer.adapter.CustomerMenuAdapter;
import com.example.welle.ui.staff.adapter.FoodTypeAdapter;

import java.util.Arrays;
import java.util.List;

public class CustomerMenuActivity extends AppCompatActivity {

    private RecyclerView recyclerFoodType;
    private RecyclerView recyclerFoodList;
    private CustomerMenuAdapter menuAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ====== 上方功能表 ======
        Button btn = findViewById(R.id.btnback);
        Button btn4 = findViewById(R.id.btncustomernotice);
        Button popupButton = findViewById(R.id.btncustomerfullmenu);

        btn.setOnClickListener(v -> startActivity(new Intent(this, CustomerMainActivity.class)));
        btn4.setOnClickListener(v -> startActivity(new Intent(this, CustomerNoticeActivity.class)));

        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CustomerMenuActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.customermenu, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.customermainch) {
                    startActivity(new Intent(this, CustomerMainActivity.class));
                } else if (item.getItemId() == R.id.customerbookch) {
                    startActivity(new Intent(this, CustomerBookActivity.class));
                } else if (item.getItemId() == R.id.customermenuch) {
                    startActivity(new Intent(this, CustomerMenuActivity.class));
                } else if (item.getItemId() == R.id.customernoticeuch) {
                    startActivity(new Intent(this, CustomerNoticeActivity.class));
                } else if (item.getItemId() == R.id.customerpreferencech) {
                    startActivity(new Intent(this, CustomerPrefActivity.class));
                } else if (item.getItemId() == R.id.customerhelpch) {
                    Toast.makeText(this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (item.getItemId() == R.id.customersignoutch) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });

        // ====== 下方 FoodType + FoodList ======
        recyclerFoodType = findViewById(R.id.recyclerFoodType);
        recyclerFoodList = findViewById(R.id.recyclerFoodItem);

        recyclerFoodType.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerFoodList.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 Adapter（一次就好）
        menuAdapter = new CustomerMenuAdapter(List.of(), menu -> {
            if ("SET".equalsIgnoreCase(menu.type)) {
                Toast.makeText(this, "查看套餐: " + menu.name, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerFoodList.setAdapter(menuAdapter);

        // 食物類型橫列
        List<String> typeList = Arrays.asList("Lunch", "Dinner", "A la carte", "Desserts", "Drinks");
        FoodTypeAdapter typeAdapter = new FoodTypeAdapter(typeList, this::loadMenuByCategory);
        recyclerFoodType.setAdapter(typeAdapter);

        // 預設顯示 Dinner
        loadMenuByCategory("Dinner");
    }

    private void loadMenuByCategory(String category) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            List<Menu> items = db.menuDao().getMenuByCategory(category);





            // 如果是套餐，查出細項
            for (Menu m : items) {
                if ("SET".equalsIgnoreCase(m.type)) {
                    List<MenuDetail> details = db.menuDetailDao().getDetailsByMenuId(m.id);
                    m.details = details;
                }
            }

            runOnUiThread(() -> menuAdapter.updateData(items)); // 🔹 用 updateData 更新
        }).start();
    }
}
