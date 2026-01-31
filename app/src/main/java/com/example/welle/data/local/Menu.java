package com.example.welle.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "menu")
public class Menu {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String category;   // Lunch / Dinner / A la carte / Drinks
    public String name;       // Set A, Set B, Steak, Salad...
    public double price;
    public String type;       // "SET" or "ALA_CARTE"

    // 🔹 新增數量欄位，預設 1
    public int quantity = 1;

    // 🔹 無參數建構子 (Room 需要)
    public Menu() {}

    // 🔹 有參數建構子 (方便插入假資料)
    public Menu(String category, String name, double price, String type) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.type = type;
        this.quantity = 1; // 預設數量
    }
}