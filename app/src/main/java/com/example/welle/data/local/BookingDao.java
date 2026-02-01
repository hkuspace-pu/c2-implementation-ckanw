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
    @Query("SELECT * FROM booking WHERE date LIKE :date || '%'")
    List<Booking> getBookingsByDate(String date);


    // Get all bookings for a specific table on a given date
    @Query("SELECT * FROM booking WHERE tableName = :tableName AND date = :date")
    List<Booking> getBookingsByTable(String tableName, String date);

    // Update the table name for a specific booking
    @Query("UPDATE booking SET tableName = :tableName WHERE id = :bookingId")
    void updateTableName(int bookingId, String tableName);

    // Get all bookings for a specific date and time slot
    @Query("SELECT * FROM booking WHERE date = :date AND time = :time")
    List<Booking> getBookingsByDateAndTime(String date, String time);

    // Get all upcoming bookings (today or later)
    @Query("SELECT * FROM booking WHERE date >= :today ORDER BY date ASC")
    List<Booking> getUpcomingBookings(String today);
}