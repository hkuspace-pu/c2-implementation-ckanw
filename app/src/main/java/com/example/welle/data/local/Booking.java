package com.example.welle.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "booking")
public class Booking {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String date;
    public String time;
    public String name;
    public String email;
    public String tel;
    public int noOfPerson;
    public String remark;
    public String eventType;
}