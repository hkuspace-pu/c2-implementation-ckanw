package com.example.welle.utils;

import com.example.welle.data.local.Booking;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class BookingUtils {

    private static final int TOTAL_2 = 10; // 可改成從設定檔或 DB 讀取
    private static final int TOTAL_4 = 6;

    /**
     * Calculate end time:
     * - Each booking occupies 3 hours.
     * - Cap at 23:00.
     */
    public static String calculateEndTime(String startTime) {
        if (startTime == null || startTime.trim().isEmpty()) {
            return "Invalid time";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date start = sdf.parse(startTime.trim());

            Calendar cal = Calendar.getInstance();
            cal.setTime(start);

            // 固定 +3 小時
            cal.add(Calendar.HOUR_OF_DAY, 3);

            // 如果超過 23:00，就直接設為 23:00
            if (cal.get(Calendar.HOUR_OF_DAY) >= 23) {
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 0);
            }

            return sdf.format(cal.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
            return "Invalid time";
        }
    }

    /**
     * Check if booking overlaps with a slot.
     */
    public static boolean isTimeOverlap(String slotStart, String slotEnd, String bookingStart, String bookingEnd) {
        if (slotStart == null || slotEnd == null || bookingStart == null || bookingEnd == null) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date slotStartTime = sdf.parse(slotStart.trim());
            Date slotEndTime = sdf.parse(slotEnd.trim());
            Date bookingStartTime = sdf.parse(bookingStart.trim());
            Date bookingEndTime = sdf.parse(bookingEnd.trim());

            return bookingStartTime.before(slotEndTime) && bookingEndTime.after(slotStartTime);

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Split table usage for bookings larger than 4 people.
     */
    public static int[] calculateTableSplit(int noOfPerson) {
        int count4 = 0;
        int count2 = 0;

        while (noOfPerson > 0) {
            if (noOfPerson >= 4) {
                count4++;
                noOfPerson -= 4;
            } else if (noOfPerson >= 2) {
                count2++;
                noOfPerson -= 2;
            } else {
                count2++;
                noOfPerson = 0;
            }
        }

        return new int[]{count4, count2};
    }

    /**
     * ✅ 檢查是否有足夠檯位（客人端 & 員工端共用）
     */
    public static boolean checkAvailability(List<Booking> bookings, String newDate, String newTime, int noOfPerson) {
        if (newTime == null || newTime.trim().isEmpty()) return false;

        String newEnd = calculateEndTime(newTime);

        int booked2 = 0, booked4 = 0;

        for (Booking b : bookings) {
            if (b.time == null) continue; // 避免 NPE
            String bookingStart = b.time.trim();
            String bookingEnd = calculateEndTime(bookingStart);

            if (isTimeOverlap(newTime, newEnd, bookingStart, bookingEnd)) {
                int[] split = calculateTableSplit(b.noOfPerson);
                booked4 += split[0];
                booked2 += split[1];
            }
        }

        int[] newSplit = calculateTableSplit(noOfPerson);

        int available4 = TOTAL_4 - booked4;
        int available2 = TOTAL_2 - booked2;

        // 如果需要 4 人檯但不足 → 用兩張 2 人檯代替
        if (newSplit[0] > 0) {
            if (available4 >= newSplit[0]) {
                available4 -= newSplit[0];
            } else {
                int shortage = newSplit[0] - available4;
                available4 = 0;
                if (available2 >= shortage * 2) {
                    available2 -= shortage * 2;
                } else {
                    return false;
                }
            }
        }

        // 檢查 2 人檯需求
        if (newSplit[1] > 0) {
            if (available2 >= newSplit[1]) {
                available2 -= newSplit[1];
            } else {
                return false;
            }
        }

        return true;
    }

    /**
     * Get today's date in yyyy-M-d format.
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * 檢查某檯位在指定日期/時間的狀態
     * - available: 可用
     * - booked: 已被訂
     * - nearly_full: 快滿（剩餘檯位數少於某閾值）
     */
    public static String getTableStatus(List<Booking> bookings, String tableName, String date, String time) {
        if (time == null || time.trim().isEmpty()) return "available";

        String slotEnd = calculateEndTime(time);

        // 檢查該檯位是否已被訂
        for (Booking b : bookings) {
            if (b.tableName != null && b.tableName.equals(tableName) && b.time != null) {
                String bookingStart = b.time.trim();
                String bookingEnd = calculateEndTime(bookingStart);

                if (isTimeOverlap(time, slotEnd, bookingStart, bookingEnd)) {
                    return "booked"; // 已被訂
                }
            }
        }

        // 檢查剩餘檯位數量，決定是否快滿
        int booked2 = 0, booked4 = 0;
        for (Booking b : bookings) {
            if (b.time == null) continue;
            String bookingStart = b.time.trim();
            String bookingEnd = calculateEndTime(bookingStart);

            if (isTimeOverlap(time, slotEnd, bookingStart, bookingEnd)) {
                int[] split = calculateTableSplit(b.noOfPerson);
                booked4 += split[0];
                booked2 += split[1];
            }
        }

        int available4 = TOTAL_4 - booked4;
        int available2 = TOTAL_2 - booked2;

        // 定義快滿邏輯：剩餘檯位少於 2 張就算快滿
        if (tableName.startsWith("Table-2") && available2 <= 2) {
            return "nearly_full";
        }
        if (tableName.startsWith("Table-4") && available4 <= 1) {
            return "nearly_full";
        }

        return "available"; // 預設可用
    }
}