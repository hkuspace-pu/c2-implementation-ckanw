package com.example.welle.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class,Booking.class,Menu.class,MenuDetail.class}, version = 8, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract BookingDao bookingDao();

    public abstract MenuDao menuDao();

    // 單例模式
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    AppDatabase.class,
                                    "welle-db"
                            )
                            .allowMainThreadQueries() // 測試用，正式建議用 background thread
                            .fallbackToDestructiveMigration() // 🔹 version 升級時避免 cras
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}