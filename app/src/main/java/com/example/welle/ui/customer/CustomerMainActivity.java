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

import com.example.welle.R;
import com.example.welle.ui.MainActivity;

public class CustomerMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.customer_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Clearer button names
        Button btnBook = findViewById(R.id.btnbook);
        Button btnSpecial = findViewById(R.id.btnspecial);
        Button btnMenu = findViewById(R.id.btnmenu);
        Button btnNotice = findViewById(R.id.btncustomernotice);
        Button btnFullMenu = findViewById(R.id.btncustomerfullmenu);

        // Book button → go to booking page
        btnBook.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerBookActivity.class)));

        // Special button → go to special offers page
        btnSpecial.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerSpecialActivity.class)));

        // Menu button → go to menu page
        btnMenu.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerMenuActivity.class)));

        // Notice button → go to notice page
        btnNotice.setOnClickListener(v ->
                startActivity(new Intent(this, CustomerNoticeActivity.class)));

        // Popup menu
        btnFullMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(CustomerMainActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.customermenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if (id == R.id.customermainch) {
                    Intent intent = new Intent(this, CustomerMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (id == R.id.customerbookch) {
                    startActivity(new Intent(this, CustomerBookActivity.class));
                } else if (id == R.id.customermenuch) {
                    startActivity(new Intent(this, CustomerMenuActivity.class));
                } else if (id == R.id.customernoticeuch) {
                    startActivity(new Intent(this, CustomerNoticeActivity.class));
                } else if (id == R.id.customerpreferencech) {
                    startActivity(new Intent(this, CustomerPrefActivity.class));
                } else if (id == R.id.customerhelpch) {
                    Toast.makeText(this, "App version: v1.0", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.customersignoutch) {
                    startActivity(new Intent(this, MainActivity.class));
                }

                return true;
            });


            popup.show();
        });
    }
}


