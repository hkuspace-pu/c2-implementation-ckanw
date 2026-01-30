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
}
