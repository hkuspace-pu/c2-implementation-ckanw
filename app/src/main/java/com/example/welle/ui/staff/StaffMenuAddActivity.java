package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.welle.R;
import com.example.welle.StaffNoticeActivity;
import com.example.welle.ui.MainActivity;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.Menu;

public class StaffMenuAddActivity extends AppCompatActivity {

    private Spinner spinnerType, spinnerSet;
    private EditText editName, editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_menu_add);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 抓取輸入欄位
        spinnerType = findViewById(R.id.spinnerType);
        spinnerSet = findViewById(R.id.spinnerSet);
        editName = findViewById(R.id.editName);
        editPrice = findViewById(R.id.editPrice);

        // 按鈕
        Button btnBack = findViewById(R.id.btnback);
        Button btnConfirm = findViewById(R.id.btnstaffconfirmmenu);
        Button btnCancel = findViewById(R.id.btnstaffcancelmenu);
        Button btnClear = findViewById(R.id.btnstaffclear);
        Button btnNotice = findViewById(R.id.btnstaffnotice);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        // 返回
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class));
        });

        // 初始化 Set Spinner
        ArrayAdapter<CharSequence> setAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.set_options,
                R.layout.spinner_item
        );
        setAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSet.setAdapter(setAdapter);

        // 初始化 Type Spinner
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.type_options,
                R.layout.spinner_item
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        // 監聽選擇事件
        spinnerSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSet = parent.getItemAtPosition(position).toString();
                Toast.makeText(StaffMenuAddActivity.this, "選擇: " + selectedSet, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // Confirm → 存入 DB
        btnConfirm.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            String set = spinnerSet.getSelectedItem().toString();
            String priceStr = editPrice.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please input full record", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                Menu menu = new Menu(set, name, price, type);
                db.menuDao().insertMenu(menu);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Record saved!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class));
                });
            }).start();
        });

        // Cancel → 不存 DB，直接返回
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "Record is no change!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(StaffMenuAddActivity.this, StaffMenuActivity.class));
        });

        // Clear → 清空輸入欄位
        btnClear.setOnClickListener(v -> {
            editName.setText("");
            editPrice.setText("");
            spinnerType.setSelection(0);
            spinnerSet.setSelection(0);
            Toast.makeText(this, "Record is Clear!", Toast.LENGTH_SHORT).show();
        });

        // Notice
        btnNotice.setOnClickListener(v -> {
            startActivity(new Intent(StaffMenuAddActivity.this, StaffNoticeActivity.class));
        });

        // Popup Menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffMenuAddActivity.this, v);
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
    }
}