package com.example.welle.model;

public class BookingSlot {
    public String timeRange;
    public int empty2;
    public int empty4;
    public int booking2;
    public int booking4;

    public BookingSlot(String timeRange, int empty2, int empty4, int booking2, int booking4) {
        this.timeRange = timeRange;
        this.empty2 = empty2;
        this.empty4 = empty4;
        this.booking2 = booking2;
        this.booking4 = booking4;
    }
}

