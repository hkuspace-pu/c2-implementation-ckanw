package com.example.welle.data.local;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu_detail",
        foreignKeys = @ForeignKey(entity = Menu.class,
                parentColumns = "id",
                childColumns = "menuId",
                onDelete = CASCADE))
public class MenuDetail {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int menuId;        // FK → Menu.id
    public String foodName;   // e.g. Soup, Main Dish, Dessert
    public int quantity;
}

