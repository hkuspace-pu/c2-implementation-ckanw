package com.example.welle.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import androidx.room.OnConflictStrategy;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);




    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    User getUserById(String userId);

    //Customer use email
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findUserByEmail(String email);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();
}