package com.example.transfertme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<HistoryItem> historyList;

    public HistoryAdapter(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView historyName, historyAmount, historyDate;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            historyName = itemView.findViewById(R.id.historyName);
            historyAmount = itemView.findViewById(R.id.historyAmount);
            historyDate = itemView.findViewById(R.id.historyDate);
        }

        public void bind(HistoryItem item) {
            historyName.setText(item.getName());

            String amt = String.format("%.0f F", item.getAmount());
            historyAmount.setText(amt);

            historyDate.setText(item.getDate());
        }
    }
}
