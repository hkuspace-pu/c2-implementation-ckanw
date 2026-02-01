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
    // 新增一筆
    @Insert
    void insert(MenuDetail menuDetail);

    // 更新一筆
    @Update
    void update(MenuDetail menuDetail);

    // 刪除一筆
    @Delete
    void delete(MenuDetail menuDetail);

    // 查詢某個套餐的所有細項 (同步版本)
    @Query("SELECT * FROM menu_detail WHERE menuId = :menuId")
    List<MenuDetail> getDetailsByMenuId(int menuId);

    // 查詢某個套餐的所有細項 (即時監聽版本)
    @Query("SELECT * FROM menu_detail WHERE menuId = :menuId")
    LiveData<List<MenuDetail>> getDetailsByMenuIdLive(int menuId);


    // 清空某個套餐的細項
    @Query("DELETE FROM menu_detail WHERE menuId = :menuId")
    void deleteDetailsForMenu(int menuId);
}