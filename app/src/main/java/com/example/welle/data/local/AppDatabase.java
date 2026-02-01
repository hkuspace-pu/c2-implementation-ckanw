package com.example.welle.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {User.class,Booking.class,Menu.class,MenuDetail.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract BookingDao bookingDao();

    public abstract MenuDao menuDao();

    public abstract MenuDetailDao menuDetailDao();

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
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // 🔹 第一次建立 DB 時插入假資料
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        AppDatabase database = getInstance(context);
                                        MenuDao dao = database.menuDao();
                                        MenuDetailDao detailDao = database.menuDetailDao();




                                        // 假的 Dinner 套餐
                                        dao.insertMenu(new Menu("Dinner", "Set A", 199.99, "SET"));
                                        dao.insertMenu(new Menu("Dinner", "Set B", 249.99, "SET"));

                                        // 假的 A la carte 單點
                                        dao.insertMenu(new Menu("A la carte", "Steak", 129.99, "ALA_CARTE"));
                                        dao.insertMenu(new Menu("A la carte", "Salad", 59.99, "ALA_CARTE"));

                                        // 假的 Drinks
                                        dao.insertMenu(new Menu("Drinks", "Coffee", 39.99, "ALA_CARTE"));
                                        dao.insertMenu(new Menu("Drinks", "Tea", 29.99, "ALA_CARTE"));

                                        // Set A

                                        // 🔹 插入 Set A 的細項 (使用剛剛取得的 setAId)
                                        MenuDetail detail1 = new MenuDetail();
                                        detail1.menuId = 1;
                                        detail1.foodName = "Steak";
                                        detail1.quantity = 1;
                                        detailDao.insert(detail1);

                                        MenuDetail detail2 = new MenuDetail();
                                        detail2.menuId = 1;
                                        detail2.foodName = "Coffee";
                                        detail2.quantity = 1;
                                        detailDao.insert(detail2);




                                    });
                                }
                            })

                            .build();
                }
            }
        }
        return INSTANCE;
    }
}