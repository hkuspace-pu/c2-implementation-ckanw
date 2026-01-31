package com.example.welle.ui.staff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;

import java.util.List;

public class FoodTypeAdapter extends RecyclerView.Adapter<FoodTypeAdapter.TypeViewHolder> {
    private List<String> typeList;
    private OnTypeClickListener listener;

    public interface OnTypeClickListener {
        void onTypeClick(String type);
    }

    public FoodTypeAdapter(List<String> typeList, OnTypeClickListener listener) {
        this.typeList = typeList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food_type, parent, false);
        return new TypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        String type = typeList.get(position);
        holder.txtType.setText(type);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTypeClick(type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeList.size();
    }

    static class TypeViewHolder extends RecyclerView.ViewHolder {
        TextView txtType;

        public TypeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
        }
    }
}

