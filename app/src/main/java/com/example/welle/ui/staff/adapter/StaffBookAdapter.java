package com.example.welle.ui.staff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
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
        holder.textEmpty2.setText(String.format("2人檯: %d", slot.empty2));
        holder.textEmpty4.setText(String.format("4人檯: %d", slot.empty4));
        holder.textBooking2.setText(String.format("2人檯: %d", slot.booking2));
        holder.textBooking4.setText(String.format("4人檯: %d", slot.booking4));
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