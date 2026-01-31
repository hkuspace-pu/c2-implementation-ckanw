package com.example.welle.ui.staff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.data.local.Menu;

import java.util.List;

public class SelectedFoodAdapter extends RecyclerView.Adapter<SelectedFoodAdapter.ItemViewHolder> {

    private List<Menu> selectedList;
    private OnItemClickListener listener;

    // 點擊事件介面
    public interface OnItemClickListener {
        void onItemClick(Menu menu);
    }

    public SelectedFoodAdapter(List<Menu> selectedList, OnItemClickListener listener) {
        this.selectedList = selectedList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_food, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Menu menu = selectedList.get(position);
        holder.txtName.setText(menu.name);
        holder.txtPrice.setText(String.format("$%.2f", menu.price));

        // 點一下就刪除
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}