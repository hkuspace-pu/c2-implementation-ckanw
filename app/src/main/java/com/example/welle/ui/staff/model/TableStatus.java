package com.example.welle.ui.staff.model;

public class TableStatus {
    public String tableName;
    public String statusText;
    public int statusColor;

    public TableStatus(String tableName, String statusText, int statusColor) {
        this.tableName = tableName;
        this.statusText = statusText;
        this.statusColor = statusColor;
    }
}