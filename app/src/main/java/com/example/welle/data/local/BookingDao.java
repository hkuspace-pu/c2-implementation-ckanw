package com.example.welle.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insertBooking(Booking booking);
    @Query("SELECT * FROM booking")
    List<Booking> getAllBookings();
    @Query("SELECT * FROM booking WHERE email = :userEmail")
    List<Booking> getBookingsByEmail(String userEmail);

    @Query("SELECT * FROM booking WHERE id = :id LIMIT 1")
    Booking getBookingById(int id);

    @Update
    void updateBooking(Booking booking);

    @Delete
    void deleteBooking(Booking booking);


    // Get all bookings for a specific date
    @Query("SELECT * FROM booking WHERE date = :date")
    List<Booking> getBookingsByDate(String date);
    // 🔹 新增：查詢某檯位的所有訂位
    @Query("SELECT * FROM booking WHERE tableName = :tableName AND date = :date")
    List<Booking> getBookingsByTable(String tableName, String date);

    // 更新某筆訂位的檯位
    @Query("UPDATE booking SET tableName = :tableName WHERE id = :bookingId")
    void updateTableName(int bookingId, String tableName);


    // Optional: Get all bookings for a specific date and time slot
    @Query("SELECT * FROM booking WHERE date = :date AND time = :time")
    List<Booking> getBookingsByDateAndTime(String date, String time);



}