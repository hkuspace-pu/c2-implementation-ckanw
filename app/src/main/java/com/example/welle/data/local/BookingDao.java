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




}