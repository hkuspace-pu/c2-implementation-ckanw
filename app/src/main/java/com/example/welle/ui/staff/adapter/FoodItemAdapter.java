package com.example.welle.ui.staff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.data.local.Menu;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ItemViewHolder> {
    private List<Menu> itemList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Menu menu);
    }

    public FoodItemAdapter(List<Menu> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Menu item = itemList.get(position);

        holder.txtName.setText(item.name);
        holder.txtPrice.setText("$" + item.price);
        holder.txtQuantity.setText(String.valueOf(item.quantity));

        // 加號按鈕
        holder.btnPlus.setOnClickListener(v -> {
            item.quantity++;
            holder.txtQuantity.setText(String.valueOf(item.quantity));
        });

        // 減號按鈕
        holder.btnMinus.setOnClickListener(v -> {
            if (item.quantity > 0) {
                item.quantity--;
                holder.txtQuantity.setText(String.valueOf(item.quantity));
            }
        });

        // 點擊整個 item → 回傳選取的食物
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtQuantity;
        Button btnPlus, btnMinus;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
        }
    }
}