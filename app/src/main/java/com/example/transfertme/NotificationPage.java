package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationPage extends AppCompatActivity {

    private ImageView notificationBtn;
    private TextView notificationTextView;
    private TextView dateTextView;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_page);

        notificationTextView = findViewById(R.id.NotificationTextView);
        dateTextView = findViewById(R.id.DateTextView);
        notificationBtn = findViewById(R.id.ImageView0101);
        db = FirebaseFirestore.getInstance();

        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationPage.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Récupérer les messages de Firestore
        retrieveMessagesFromFirestore();
    }

    private void retrieveMessagesFromFirestore() {
        // Récupérer la collection "Notification" de Firestore
        db.collection("Notification")
                .document("tc86xHdAnS5r1MknPk7l") // Remplacez "tc86xHdAnS5r1MknPk7l" par l'ID réel du document
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Vérifier si le document existe
                        if (task.getResult().exists()) {
                            // Récupérer le document
                            DocumentSnapshot document = task.getResult();

                            // Récupérer les valeurs des champs "message" et "date"
                            String message = document.getString("message");
                            Timestamp timestamp = document.getTimestamp("date");
                            String dateString = formatTimestamp(timestamp);

                            // Afficher le message et la date dans les TextView correspondants
                            runOnUiThread(() -> {
                                notificationTextView.setText(message);
                                dateTextView.setText(dateString);
                            });
                        }
                    } else {
                        // Gérer les erreurs de récupération des messages depuis Firestore
                    }
                });
    }

    private String formatTimestamp(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = timestamp.toDate();
        return dateFormat.format(date);
    }

}
