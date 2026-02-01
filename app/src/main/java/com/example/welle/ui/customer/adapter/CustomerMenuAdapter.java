package com.example.welle.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.data.local.Menu;
import com.example.welle.data.local.MenuDetail;

import java.util.List;

public class CustomerMenuAdapter extends RecyclerView.Adapter<CustomerMenuAdapter.MenuViewHolder> {

    private List<Menu> menuList;
    private OnSetClickListener setClickListener;

    // 只有套餐可以點擊
    public interface OnSetClickListener {
        void onSetClick(Menu menu);
    }

    public CustomerMenuAdapter(List<Menu> menuList, OnSetClickListener listener) {
        this.menuList = menuList;
        this.setClickListener = listener;
    }

    // 🔹 新增更新方法
    public void updateData(List<Menu> newList) {
        this.menuList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customer_menu, parent, false); // ✅ 使用 item layout
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu item = menuList.get(position);

        holder.txtName.setText(item.name);
        holder.txtPrice.setText("$" + item.price);

        // 如果是套餐 → 顯示細項
        if ("SET".equalsIgnoreCase(item.type)) { // 🔹 忽略大小寫
            holder.txtDetails.setVisibility(View.VISIBLE);

            StringBuilder details = new StringBuilder();
            if (item.details != null && !item.details.isEmpty()) {
                for (MenuDetail d : item.details) {
                    details.append(d.foodName).append(", ");
                }
                if (details.length() > 2) {
                    details.setLength(details.length() - 2); // 去掉最後的逗號
                }
            }
            holder.txtDetails.setText("Set Detail: " + details.toString());

            // 套餐可以點擊
            holder.itemView.setOnClickListener(v -> {
                if (setClickListener != null) setClickListener.onSetClick(item);
            });

        } else {
            // 單點 → 不顯示細項，不可點擊
            holder.txtDetails.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return menuList != null ? menuList.size() : 0;
    }

    static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtDetails;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDetails = itemView.findViewById(R.id.txtDetails);
        }
    }
}