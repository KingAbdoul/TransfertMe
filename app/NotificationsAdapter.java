package com.example.transfertme;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationsAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        NotificationItem item = notificationList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView messageText, dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.notificationMessage);
            dateText = itemView.findViewById(R.id.notificationDate);
        }

        public void bind(NotificationItem item) {
            messageText.setText(item.getMessage());
            // Formatage de la date
            String dateFormatted = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(new Date(item.getTimestamp()));
            dateText.setText(dateFormatted);
        }
    }
}
