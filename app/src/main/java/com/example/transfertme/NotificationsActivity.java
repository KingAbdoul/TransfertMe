package com.example.transfertme;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/** Import Firestore **/
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView recyclerNotifications;
    private NotificationsAdapter adapter;
    private List<NotificationItem> notificationList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        recyclerNotifications.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationsAdapter(notificationList);
        recyclerNotifications.setAdapter(adapter);

        loadNotifications();
    }

    private void loadNotifications() {
        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            return;
        }

        // On suppose que dans Firestore on a la collection "notifications"
        // où chaque document a un "userId" pour savoir à qui est destinée la notif
        CollectionReference notifRef = db.collection("notifications");

        notifRef
                .whereEqualTo("userId", currentUser.getUid())
                // .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        notificationList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            NotificationItem item = doc.toObject(NotificationItem.class);
                            notificationList.add(item);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Erreur de chargement des notifications", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
