package com.example.welle.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface MenuDao {
    @Query("SELECT * FROM menu WHERE category = :category")
    List<Menu> getMenuByCategory(String category);

    @Insert
    long insertMenu(Menu menu);

    @Update
    void updateMenu(Menu menu);

    @Delete
    void deleteMenu(Menu menu);

    // 套餐細項
    @Query("SELECT * FROM menu_detail WHERE menuId = :menuId")
    List<MenuDetail> getMenuDetails(int menuId);

    @Insert
    void insertMenuDetail(MenuDetail detail);

    @Insert
    void insertMenus(List<Menu> menus);

    @Insert
    void insertMenuDetails(List<MenuDetail> details);


    @Update
    void updateMenuDetail(MenuDetail detail);

    @Delete
    void deleteMenuDetail(MenuDetail detail);

    @Query("SELECT * FROM menu")
    List<Menu> getAllMenu();

    @Query("SELECT * FROM menu WHERE id = :id LIMIT 1")
    Menu getMenuById(int id);

    @Query("SELECT * FROM menu WHERE category = :category")
    LiveData<List<Menu>> getMenuByCategoryLive(String category);



}
