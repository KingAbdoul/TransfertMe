package com.example.transfertme;
import com.google.firebase.Timestamp;

public class Notification {
    private String message;
    private Timestamp date;

    public Notification() {
        // Constructeur par défaut requis pour la désérialisation de Firestore
    }

    public Notification(String message, Timestamp date) {
        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public Timestamp getDate() {
        return date;
    }
}
