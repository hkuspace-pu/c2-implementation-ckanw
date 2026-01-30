package com.example.welle.ui.staff.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.welle.R;
import com.example.welle.ui.staff.model.TableStatus;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    public interface OnTableClickListener {
        void onTableClick(TableStatus table);
    }

    private List<TableStatus> tableList;
    private OnTableClickListener listener;

    public TableAdapter(List<TableStatus> tableList, OnTableClickListener listener) {
        this.tableList = tableList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_table, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        TableStatus table = tableList.get(position);
        holder.txtName.setText(table.tableName);
        holder.txtStatus.setText(table.statusText);

        holder.itemView.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), table.statusColor)
        );

        holder.itemView.setOnClickListener(v -> listener.onTableClick(table));
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    static class TableViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtStatus;

        public TableViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtTableName);
            txtStatus = itemView.findViewById(R.id.txtTableStatus);
        }
    }
}