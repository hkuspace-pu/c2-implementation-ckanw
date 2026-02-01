package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.adapter.TableAdapter;
import com.example.welle.ui.staff.model.TableStatus;
import com.example.welle.data.local.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class StaffSelectTableActivity extends AppCompatActivity implements TableAdapter.OnTableClickListener {

    private RecyclerView recyclerTables;
    private List<TableStatus> tableList;
    private TableStatus selectedTable;

    private TextView txtSelectedDate, txtSelectedTime;
    private Button btnConfirmTable;

    private String selectedDate;
    private String selectedTime;
    private int bookingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.staff_select_table);

        // Bind UI elements
        recyclerTables = findViewById(R.id.recyclerTables);
        txtSelectedDate = findViewById(R.id.txtSelectedDate);
        txtSelectedTime = findViewById(R.id.txtSelectedTime);
        btnConfirmTable = findViewById(R.id.btnConfirmTable);

        Button btnBack = findViewById(R.id.btnback);
        Button btnMenu = findViewById(R.id.btnstaffullmenu);
        Button btnNotice = findViewById(R.id.btnstaffnotice);

        recyclerTables.setLayoutManager(new GridLayoutManager(this, 4));

        // Receive data from previous activity
        Intent intent = getIntent();
        selectedDate = intent.getStringExtra("selectedDate");
        selectedTime = intent.getStringExtra("selectedTime");
        bookingId = intent.getIntExtra("bookingId", -1);

        if (selectedDate != null) txtSelectedDate.setText(selectedDate);
        if (selectedTime != null) txtSelectedTime.setText(selectedTime);

        // Mock table data
        tableList = new ArrayList<>();
        tableList.add(new TableStatus("11A", "", R.color.gray));
        tableList.add(new TableStatus("11B", "", R.color.gray));
        tableList.add(new TableStatus("12A", "30min", R.color.teal_200));
        tableList.add(new TableStatus("12B", "30min", R.color.teal_200));
        tableList.add(new TableStatus("13A", "", R.color.gray));
        tableList.add(new TableStatus("13B", "", R.color.gray));
        tableList.add(new TableStatus("13C", "", R.color.gray));
        tableList.add(new TableStatus("14A", "15min", R.color.teal_200));
        tableList.add(new TableStatus("14B", "15min", R.color.teal_200));
        tableList.add(new TableStatus("14C", "15min", R.color.teal_200));
        tableList.add(new TableStatus("15A", "", R.color.gray));
        tableList.add(new TableStatus("15B", "", R.color.gray));
        tableList.add(new TableStatus("15C", "", R.color.gray));
        tableList.add(new TableStatus("16A", "15min", R.color.teal_200));
        tableList.add(new TableStatus("16B", "15min", R.color.teal_200));
        tableList.add(new TableStatus("16C", "15min", R.color.teal_200));
        tableList.add(new TableStatus("16D", "", R.color.gray));

        TableAdapter adapter = new TableAdapter(tableList, this);
        recyclerTables.setAdapter(adapter);

        // Confirm button → update DB
        btnConfirmTable.setOnClickListener(v -> {
            if (selectedTable == null) {
                Toast.makeText(this, "Please select a table first", Toast.LENGTH_SHORT).show();
                return;
            }

            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                db.bookingDao().updateTableName(bookingId, selectedTable.tableName);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Table updated: " + selectedTable.tableName, Toast.LENGTH_SHORT).show();
                    // Return to booking list page
                    Intent backIntent = new Intent(StaffSelectTableActivity.this, StaffBookActivity.class);
                    startActivity(backIntent);
                    finish();
                });
            }).start();
        });

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Notice button
        btnNotice.setOnClickListener(v -> startActivity(new Intent(this, StaffNoticeActivity.class)));

        // Menu button → PopupMenu
        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffSelectTableActivity.this, v);
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

    @Override
    public void onTableClick(TableStatus table) {
        selectedTable = table;
        Toast.makeText(this, "Selected table: " + table.tableName, Toast.LENGTH_SHORT).show();
    }
}