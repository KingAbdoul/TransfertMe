package com.example.transfertme;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

public class HistoriquesActivity extends AppCompatActivity {

    private ImageView closeIcon;
    private RecyclerView recyclerHistory;

    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private HistoryAdapter historyAdapter;
    private List<HistoryItem> historyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historiques); // Le layout ci-dessus

        // 1. Initialiser Firestore et l'utilisateur
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Récupérer les vues
        closeIcon = findViewById(R.id.closeIcon);
        recyclerHistory = findViewById(R.id.recyclerHistory);

        // 3. Icône fermer
        if (closeIcon != null) {
            closeIcon.setOnClickListener(v -> finish());
        }

        // 4. Configurer le RecyclerView
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        historyList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyList);
        recyclerHistory.setAdapter(historyAdapter);

        // 5. Charger la liste des transactions
        loadAllTransactions();
    }

    private void loadAllTransactions() {
        // Chemin : users/{userId}/transactions
        CollectionReference transactionsRef = db
                .collection("users")
                .document(currentUser.getUid())
                .collection("transactions");

        // On récupère TOUTES les transactions
        transactionsRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    historyList.clear(); // Vider l'ancienne liste
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        // doc contient : "senderNumber", "amount", "dateTime", ...
                        // On crée un HistoryItem
                        HistoryItem item = new HistoryItem();

                        // Récupérer le "senderNumber" comme "name"
                        String name = doc.getString("senderNumber");
                        Double amount = doc.getDouble("amount");
                        String dateTime = doc.getString("dateTime");

                        item.setName(name != null ? name : "Inconnu");
                        item.setAmount(amount != null ? amount : 0.0);
                        item.setDate(dateTime != null ? dateTime : "Pas de date");

                        // Ajouter à la liste
                        historyList.add(item);
                    }
                    // Notifier l'adapter
                    historyAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(HistoriquesActivity.this,
                            "Erreur lors du chargement : " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("HistoriquesActivity", "loadAllTransactions: ", e);
                });
    }
}
