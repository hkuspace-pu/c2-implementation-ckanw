package com.example.welle.ui.staff.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.ui.staff.StaffBookDetailActivity;
import com.example.welle.model.BookingSlot;

import java.util.List;

public class StaffBookAdapter extends RecyclerView.Adapter<StaffBookAdapter.ViewHolder> {

    private List<BookingSlot> slots;

    public StaffBookAdapter(List<BookingSlot> slots) {
        this.slots = slots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.staff_bookingslot_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingSlot slot = slots.get(position);
        holder.textTime.setText(slot.timeRange);

        // Display remaining 2-person tables
        int empty2 = slot.empty2;
        holder.textEmpty2.setText(String.format("2: %d", empty2));
        if (empty2 == 12) { // Completely empty → original color
            holder.textEmpty2.setTextColor(Color.YELLOW);
        } else if (empty2 <= 3) { // 3 tables or fewer left → red
            holder.textEmpty2.setTextColor(Color.RED);
        } else { // Reduced but more than 3 → white
            holder.textEmpty2.setTextColor(Color.WHITE);
        }

        // Display remaining 4-person tables
        int empty4 = slot.empty4;
        holder.textEmpty4.setText(String.format("4: %d", empty4));
        if (empty4 == 6) { // Completely empty → original color
            holder.textEmpty4.setTextColor(Color.YELLOW);
        } else if (empty4 <= 2) { // 2 tables or fewer left → red
            holder.textEmpty4.setTextColor(Color.RED);
        } else { // Reduced but more than 2 → white
            holder.textEmpty4.setTextColor(Color.WHITE);
        }

        // Display booked tables (keep original color)
        holder.textBooking2.setText(String.format("2: %d", slot.booking2));
        holder.textBooking4.setText(String.format("4: %d", slot.booking4));

        // 🔹 Click event → Navigate to StaffBookDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, StaffBookDetailActivity.class);
            intent.putExtra("selectedTimeRange", slot.timeRange);
            intent.putExtra("empty2", slot.empty2);
            intent.putExtra("empty4", slot.empty4);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return slots != null ? slots.size() : 0;
    }

    public void setSlots(List<BookingSlot> slots) {
        this.slots = slots;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textEmpty2, textEmpty4, textBooking2, textBooking4;

        ViewHolder(View itemView) {
            super(itemView);
            textTime = itemView.findViewById(R.id.textTime);
            textEmpty2 = itemView.findViewById(R.id.textEmpty2);
            textEmpty4 = itemView.findViewById(R.id.textEmpty4);
            textBooking2 = itemView.findViewById(R.id.textBooking2);
            textBooking4 = itemView.findViewById(R.id.textBooking4);
        }
    }
}