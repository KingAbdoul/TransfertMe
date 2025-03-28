package com.example.transfertme;

public class NotificationItem {
    private String message;
    private long timestamp;
    private String userId;

    public NotificationItem() {
    }

    public NotificationItem(String message, long timestamp, String userId) {
        this.message = message;
        this.timestamp = timestamp;
        this.userId = userId;
    }


    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
