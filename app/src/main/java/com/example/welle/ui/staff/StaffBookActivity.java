package com.example.welle.ui.staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.example.welle.data.local.Booking;
import com.example.welle.data.local.BookingDao;
import com.example.welle.model.BookingSlot;
import com.example.welle.StaffNoticeActivity;
import com.example.welle.ui.MainActivity;
import com.example.welle.ui.staff.adapter.StaffBookAdapter;
import com.example.welle.utils.BookingUtils;

import java.util.ArrayList;
import java.util.List;

public class StaffBookActivity extends AppCompatActivity {

    private RecyclerView recyclerBooking;
    private StaffBookAdapter adapter;
    private BookingDao bookingDao;

    // Time slots
    private final String[][] TIMES = {
            {"17:00", "18:00"},
            {"18:00", "19:00"},
            {"19:00", "20:00"},
            {"20:00", "21:00"},
            {"21:00", "22:00"},
            {"22:00", "23:00"}
    };

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
        Button btn3 = findViewById(R.id.btnstaffnotice);
        Button popupButton = findViewById(R.id.btnstaffullmenu);
        CalendarView calendarView = findViewById(R.id.calendarView2);

        // Back to main
        btn.setOnClickListener(v -> startActivity(new Intent(StaffBookActivity.this, StaffMainActivity.class)));

        // Go to notice
        btn3.setOnClickListener(v -> startActivity(new Intent(StaffBookActivity.this, StaffNoticeActivity.class)));

        // Popup menu
        popupButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(StaffBookActivity.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.staffmenu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.staffmainch) {
                    startActivity(new Intent(this, StaffMainActivity.class));
                } else if (id == R.id.staffnoticech) {
                    startActivity(new Intent(this, StaffNoticeActivity.class));
                } else if (id == R.id.staffbookch) {
                    startActivity(new Intent(this, StaffBookActivity.class));
                } else if (id == R.id.staffmenuch) {
                    startActivity(new Intent(this, StaffMenuActivity.class));
                } else if (id == R.id.staffhelpch) {
                    Toast.makeText(this, "Version v1.0", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.staffsignoutch) {
                    startActivity(new Intent(this, MainActivity.class));
                }
                return true;
            });
            popup.show();
        });

        // Booking slots
        recyclerBooking = findViewById(R.id.recyclerBooking);
        recyclerBooking.setLayoutManager(new LinearLayoutManager(this));

        bookingDao = AppDatabase.getInstance(getApplicationContext()).bookingDao();

        adapter = new StaffBookAdapter(new ArrayList<>());
        recyclerBooking.setAdapter(adapter);

        // Default show today
        updateSlots(BookingUtils.getTodayDate());

        // Update when selecting date
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            updateSlots(selectedDate);
        });
    }

    private void updateSlots(String selectedDate) {
        List<BookingSlot> slotList = new ArrayList<>();
        List<Booking> bookings = bookingDao.getBookingsByDate(selectedDate);

        for (String[] timeRange : TIMES) {
            String slotStart = timeRange[0];
            String slotEnd = timeRange[1];

            int booked2 = 0, booked4 = 0;

            for (Booking b : bookings) {
                String bookingStart = b.time.trim();
                String bookingEnd = BookingUtils.calculateEndTime(bookingStart);

                boolean overlap = BookingUtils.isTimeOverlap(slotStart, slotEnd, bookingStart, bookingEnd);

                if (overlap) {
                    // 直接用 BookingUtils.calculateTableSplit()，再套用 checkAvailability 的邏輯
                    int[] split = BookingUtils.calculateTableSplit(b.noOfPerson);

                    // 嘗試扣除 4 人檯，不足時用 2 人檯補
                    int available4 = 6 - booked4;
                    int available2 = 10 - booked2;

                    if (split[0] > 0) {
                        if (available4 >= split[0]) {
                            booked4 += split[0];
                        } else {
                            int shortage = split[0] - available4;
                            booked4 += available4;
                            booked2 += shortage * 2;
                        }
                    }

                    if (split[1] > 0) {
                        booked2 += split[1];
                    }
                }
            }

            int empty2 = 10 - booked2;
            int empty4 = 6 - booked4;

            slotList.add(new BookingSlot(slotStart + " - " + slotEnd, empty2, empty4, booked2, booked4));
        }

        adapter.setSlots(slotList);
    }
}