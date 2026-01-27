package com.example.welle.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.data.local.Booking;

import java.util.List;

public class CustomerNoticeAdapter extends RecyclerView.Adapter<CustomerNoticeAdapter.NoticeViewHolder> {

    private List<Booking> bookingList;
    private OnItemClickListener listener;

    // 建構子
    public CustomerNoticeAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    // 定義介面
    public interface OnItemClickListener {
        void onItemClick(Booking booking);
    }

    // 提供設定方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_notice_layout, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Title: 日期 + 時間
        holder.itemTitle.setText(booking.date + " " + booking.time);

        // Subtitle: 姓名 + 人數 + remark + eventType
        String subtitle = booking.name
                + " | " + booking.noOfPerson + " persons"
                + " | " + (booking.remark != null ? booking.remark : "")
                + " | " + (booking.eventType != null ? booking.eventType : "");
        holder.itemSubtitle.setText(subtitle);

        // 點擊事件交給 listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(booking);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList != null ? bookingList.size() : 0;
    }

    // ViewHolder
    static class NoticeViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemSubtitle;

        NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemSubtitle = itemView.findViewById(R.id.item_subtitle);
        }
    }
}