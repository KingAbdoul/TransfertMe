package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryPage extends AppCompatActivity {

    private ImageView historyBtn;
    private ListView transactionListView;

    private List<HistoryTransaction> transactionList;
    private DatabaseReference historyReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_page);

        mAuth = FirebaseAuth.getInstance();

        // Récupération des références des vues depuis le layout XML
        historyBtn = findViewById(R.id.ImageView0101);
        transactionListView = findViewById(R.id.transactionListView);

        // Configuration du bouton d'historique pour retourner à la page d'accueil
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryPage.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Initialisation de la liste des transactions et de la référence à la base de données
        transactionList = new ArrayList<>();
        historyReference = FirebaseDatabase.getInstance().getReference().child("History");

        // Obtenir l'identifiant unique de l'utilisateur actuellement connecté
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();

            // Écouteur d'événement pour récupérer les données de l'historique depuis la base de données
            historyReference.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Réinitialisation de la liste des transactions
                    transactionList.clear();

                    // Parcours des données de la base de données pour construire la liste des transactions
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HistoryTransaction historyTransaction = snapshot.getValue(HistoryTransaction.class);
                        if (historyTransaction != null) {
                            transactionList.add(historyTransaction);

                            // Ajoutez des instructions Log pour vérifier les données
                            Log.d("TransactionData", "Sender: " + historyTransaction.getSenderNumber());
                            Log.d("TransactionData", "Receiver: " + historyTransaction.getReceiverNumber());
                            // Ajoutez d'autres données de transaction ici
                        }
                    }

                    // Création de l'adaptateur et liaison avec la liste des transactions
                    TransactionAdapter adapter = new TransactionAdapter(HistoryPage.this, transactionList);
                    transactionListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Gestion de l'erreur lors de la récupération des données
                    Toast.makeText(HistoryPage.this, "Aurevoir", Toast.LENGTH_SHORT).show();
                    Log.e("HistoryPage", "Erreur Firebase: " + databaseError.getMessage());
                }
            });
        }
    }
}
