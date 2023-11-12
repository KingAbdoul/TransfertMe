package com.example.transfertme;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Cancel_A_Transfer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner referenceSpinner;
    private Button cancelButton;
    private DatabaseReference transactionRef;
    private ImageView ImageView0106;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_atransfer);

        referenceSpinner = findViewById(R.id.send_Spinner);
        cancelButton = findViewById(R.id.cancelButton);
        ImageView0106 = findViewById(R.id.ImageView0106);

        // Référence à la base de données Firebase
        transactionRef = FirebaseDatabase.getInstance().getReference("Transaction");

        // Récupérer les références des dernières transactions d'argent
        getTransactionReferences();

        // Gestionnaire du clic sur le bouton "Cancel"
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Vérifier si un élément est sélectionné dans le Spinner
                if (referenceSpinner.getSelectedItem() != null) {
                    cancelTransaction();
                } else {
                    Toast.makeText(Cancel_A_Transfer.this, "Veuillez sélectionner une transaction à annuler", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Gestionnaire du clic sur l'ImageView "ImageView0101"
        ImageView0106.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cancel_A_Transfer.this, SettingPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getTransactionReferences() {
        transactionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> references = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String reference = snapshot.getKey();
                    references.add(reference);
                }

                // Création de l'adaptateur pour le Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Cancel_A_Transfer.this, android.R.layout.simple_spinner_item, references);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Attribution de l'adaptateur au Spinner
                referenceSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Cancel_A_Transfer.this, "Erreur lors de la récupération des références des transactions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelTransaction() {
        String reference = referenceSpinner.getSelectedItem().toString();
        transactionRef.child(reference).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Cancel_A_Transfer.this, "Transfert annulé avec succès", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Cancel_A_Transfer.this, "Erreur lors de l'annulation du transfert", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Ne rien faire lorsque rien n'est sélectionné
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Ne rien faire lorsque rien n'est sélectionné
    }
}
