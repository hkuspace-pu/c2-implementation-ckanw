package com.example.welle.ui.staff.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.data.local.Booking;

import java.util.List;

public class StaffNoticeAdapter extends RecyclerView.Adapter<StaffNoticeAdapter.NoticeViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    public StaffNoticeAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    public void updateData(List<Booking> newList) {
        this.bookingList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_staff_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // 綁定資料
        holder.txtDateTime.setText(booking.date + " " + booking.time);
        holder.txtName.setText("客人: " + booking.name);
        holder.txtEmail.setText("Email: " + booking.email);
        holder.txtRemark.setText("備註: " + (booking.remark != null ? booking.remark : "無"));
        holder.txtEventType.setText("事件: " + (booking.eventType != null ? booking.eventType : "一般"));
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView txtDateTime, txtName, txtEmail, txtRemark, txtEventType;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            txtName = itemView.findViewById(R.id.txtName);
            txtEmail = itemView.findViewById(R.id.txtEmail);
            txtRemark = itemView.findViewById(R.id.txtRemark);
            txtEventType = itemView.findViewById(R.id.txtEventType);
        }
    }
}