package com.example.welle.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MenuDetailDao {
    @Insert
    void insert(MenuDetail menuDetail);

    @Update
    void update(MenuDetail menuDetail);

    @Delete
    void delete(MenuDetail menuDetail);

    @Query("SELECT * FROM menu_detail WHERE menuId = :menuId")
    List<MenuDetail> getDetailsForMenu(int menuId);
}
