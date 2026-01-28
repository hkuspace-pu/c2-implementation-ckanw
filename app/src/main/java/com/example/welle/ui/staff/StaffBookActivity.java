package com.example.welle.ui.staff;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.model.BookingSlot;
import com.example.welle.StaffBookHeaderActivity;
import com.example.welle.StaffMenuActivity;
import com.example.welle.StaffNoticeActivity;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.adapter.StaffBookAdapter;

import java.util.ArrayList;
import java.util.List;

public class StaffBookActivity extends AppCompatActivity {

    private RecyclerView recyclerBooking;
    private StaffBookAdapter adapter;
    private List<BookingSlot> slotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_book);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.btnback);
        //Button btn2 = findViewById(R.id.btnstaffbkdate);
        Button btn3 = findViewById(R.id.btnstaffnotice);
        Button popupButton = findViewById(R.id.btnstaffullmenu);

        // 返回主頁
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(StaffBookActivity.this, StaffMainActivity.class);
            startActivity(intent);
        });

        // 跳去 BookHeader
        //btn2.setOnClickListener(v -> {
        //   Intent intent = new Intent(StaffBookActivity.this, StaffBookHeaderActivity.class);
         //   startActivity(intent);
        //});

        // 跳去 Notice
        btn3.setOnClickListener(v -> {
            Intent intent = new Intent(StaffBookActivity.this, StaffNoticeActivity.class);
            startActivity(intent);
        });

        // Popup Menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffBookActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.staffmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.staffmainch) {
                    startActivity(new Intent(StaffBookActivity.this, StaffMainActivity.class));
                } else if (id == R.id.staffnoticech) {
                    startActivity(new Intent(StaffBookActivity.this, StaffNoticeActivity.class));
                } else if (id == R.id.staffbookch) {
                    startActivity(new Intent(StaffBookActivity.this, StaffBookActivity.class));
                } else if (id == R.id.staffmenuch) {
                    startActivity(new Intent(StaffBookActivity.this, StaffMenuActivity.class));
                } else if (id == R.id.staffhelpch) {
                    Toast.makeText(StaffBookActivity.this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.staffsignoutch) {
                    startActivity(new Intent(StaffBookActivity.this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });

        // ===== Booking 時段表 =====
        recyclerBooking = findViewById(R.id.recyclerBooking);
        recyclerBooking.setLayoutManager(new LinearLayoutManager(this));

        slotList = new ArrayList<>();
        // 假資料示例
        slotList.add(new BookingSlot("11:00 - 12:00", 10, 6, 0, 0));
        slotList.add(new BookingSlot("12:00 - 13:00", 9, 6, 1, 0));
        slotList.add(new BookingSlot("13:00 - 14:00", 5, 6, 5, 0));
        slotList.add(new BookingSlot("14:00 - 15:00", 4, 6, 4, 0));
        slotList.add(new BookingSlot("15:00 - 16:00", 5, 6, 2, 0));
        slotList.add(new BookingSlot("16:00 - 17:00", 6, 6, 3, 0));
        slotList.add(new BookingSlot("17:00 - 18:00", 7, 6, 1, 0));
        slotList.add(new BookingSlot("18:00 - 19:00", 6, 6, 3, 0));
        slotList.add(new BookingSlot("19:00 - 20:00", 4, 6, 0, 2));
        slotList.add(new BookingSlot("20:00 - 21:00", 0, 6, 0, 2));

        adapter = new StaffBookAdapter(slotList);
        recyclerBooking.setAdapter(adapter);
    }
}