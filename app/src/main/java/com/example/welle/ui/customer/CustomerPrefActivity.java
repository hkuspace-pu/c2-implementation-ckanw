package com.example.welle.ui.customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.welle.R;
import com.example.welle.data.local.AppDatabase;
import com.example.welle.data.local.User;

public class CustomerPrefActivity extends AppCompatActivity {

    private AppDatabase db;
    private EditText editFirstname, editLastname, editEmail, editContact;
    private Button btnConfirm, btnCancel;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_pref);

        db = AppDatabase.getInstance(getApplicationContext());

        editFirstname = findViewById(R.id.editTextFirstName);
        editLastname = findViewById(R.id.editTextLastName);
        editEmail = findViewById(R.id.editTextEmail);
        editContact = findViewById(R.id.editTextMobile);

        btnConfirm = findViewById(R.id.btnconfirm);
        btnCancel = findViewById(R.id.btncancel2);

        // 取得目前登入的 email（假設你在 SharedPreferences 存了登入資訊）
        String loginEmail = getSharedPreferences("login", MODE_PRIVATE)
                .getString("email", null);

        if (loginEmail != null) {
            new Thread(() -> {
                // ✅ 查詢前先轉小寫
                currentUser = db.userDao().findUserByEmail(loginEmail.trim().toLowerCase());
                runOnUiThread(() -> {
                    if (currentUser != null) {
                        editFirstname.setText(currentUser.firstname);
                        editLastname.setText(currentUser.lastname);
                        editEmail.setText(currentUser.email);
                        editContact.setText(currentUser.contact);
                    }
                });
            }).start();
        }

        // 確認更新
        btnConfirm.setOnClickListener(v -> {
            String firstname = editFirstname.getText().toString().trim();
            String lastname = editLastname.getText().toString().trim();
            // ✅ 更新前也轉小寫
            String email = editEmail.getText().toString().trim().toLowerCase();
            String contact = editContact.getText().toString().trim();

            new Thread(() -> {
                if (currentUser != null) {
                    // 更新已存在的用戶
                    currentUser.firstname = firstname;
                    currentUser.lastname = lastname;
                    currentUser.email = email;
                    currentUser.contact = contact;
                    db.userDao().updateUser(currentUser);
                } else {
                    // 如果沒有找到，就新增一筆
                    User newUser = new User(
                            java.util.UUID.randomUUID().toString(),
                            "offlineUser",
                            "", // password 可留空或另外處理
                            firstname,
                            lastname,
                            email,
                            contact,
                            "customer"
                    );
                    db.userDao().insertUser(newUser);
                }
            }).start();

            Toast.makeText(this, "User record updated!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CustomerMainActivity.class));
        });

        // 取消更新
        btnCancel.setOnClickListener(v -> {
            Toast.makeText(this, "No changes saved!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CustomerMainActivity.class));
        });
    }
}