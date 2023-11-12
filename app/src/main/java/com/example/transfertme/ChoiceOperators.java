package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class ChoiceOperators extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button addRecipientButton;
    private ImageView imageView0101;
    private Spinner sendSpinner;
    private EditText senderNumberEditText;
    private Spinner receiveSpinner;
    private EditText receiverNumberEditText;

    private EditText amountToSendEditText;
    private TextView feesTextView;
    private TextView recipientReceiveTextView;

    private int[] operatorImages = {R.drawable.orange, R.drawable.mtn, R.drawable.moov};
    private String[] operatorNames;

    private DatabaseReference transactionRef;
    private DatabaseReference historyRef;

    private static final double FEES_PERCENTAGE = 2.0; // Frais en pourcentage (2%)

    private FirebaseAuth mAuth; // Objet d'authentification Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_operators);

        // Initialisation des vues
        addRecipientButton = findViewById(R.id.Add_recipient);
        imageView0101 = findViewById(R.id.ImageView0101);
        sendSpinner = findViewById(R.id.send_Spinner);
        senderNumberEditText = findViewById(R.id.Sender_number);
        receiveSpinner = findViewById(R.id.receiveSpinner);
        receiverNumberEditText = findViewById(R.id.Recever_number2);
        amountToSendEditText = findViewById(R.id.amountToSendEditText);
        feesTextView = findViewById(R.id.feesTextView);
        recipientReceiveTextView = findViewById(R.id.recipientReceiveTextView);

        // Références à la base de données Firebase
        transactionRef = FirebaseDatabase.getInstance().getReference("Transaction");
        historyRef = FirebaseDatabase.getInstance().getReference("History");

        // Initialisation de l'objet d'authentification Firebase
        mAuth = FirebaseAuth.getInstance();

        // Gestionnaire du clic sur le bouton "Add_recipient"
        addRecipientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    saveTransactionToFirebase();
                    saveTransactionToHistory();
                    Intent intent = new Intent(ChoiceOperators.this,
                            OTPVerification.class);
                    startActivity(intent);
                }
            }
        });

        // Gestionnaire du clic sur l'ImageView "ImageView0101"
        imageView0101.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChoiceOperators.this, HomePage.class);
                startActivity(intent);
            }
        });

        // Récupération des noms des opérateurs depuis les ressources
        operatorNames = getResources().getStringArray(R.array.ChoiceOpereators);

        // Création de l'adaptateur personnalisé pour le Spinner
        ArrayAdapter<String> adapter = new CustomArrayAdapter(this, Arrays.asList(operatorNames), operatorImages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attribution de l'adaptateur au Spinner d'envoi
        sendSpinner.setAdapter(adapter);
        sendSpinner.setOnItemSelectedListener(this);

        // Attribution de l'adaptateur au Spinner de réception
        receiveSpinner.setAdapter(adapter);
        receiveSpinner.setOnItemSelectedListener(this);

        // Écouteur de texte pour le champ de montant à envoyer
        amountToSendEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Ne rien faire avant que le texte change
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Ne rien faire pendant que le texte change
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Mettre à jour les calculs après que le texte est changé
                calculateFeesAndRecipientReceive();
            }
        });
    }

    private boolean isInputValid() {
        String senderNumber = senderNumberEditText.getText().toString().trim();
        String receiverNumber = receiverNumberEditText.getText().toString().trim();
        String amountStr = amountToSendEditText.getText().toString().trim();

        // Vérifier si les numéros de téléphone ont une longueur maximale de 10 chiffres
        if (senderNumber.length() != 10 || receiverNumber.length() != 10) {
            Toast.makeText(ChoiceOperators.this, "Les numéros de téléphone doivent comporter 10 chiffres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Vérifier si le montant est compris entre 1000 et 500000
        if (amountStr.isEmpty()) {
            Toast.makeText(ChoiceOperators.this, "Veuillez saisir le montant", Toast.LENGTH_SHORT).show();
            return false;
        }

        double amount = Double.parseDouble(amountStr);

        if (amount < 1000 || amount > 500000) {
            Toast.makeText(ChoiceOperators.this, "Le montant doit être compris entre 1000 et 500000", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (senderNumber.isEmpty() || receiverNumber.isEmpty()) {
            Toast.makeText(ChoiceOperators.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void calculateFeesAndRecipientReceive() {
        String amountStr = amountToSendEditText.getText().toString().trim();
        if (!amountStr.isEmpty()) {
            double amount = Double.parseDouble(amountStr);

            // Calculer les frais (2% du montant)
            double fees = amount * (FEES_PERCENTAGE / 100.0);

            // Calculer le montant restant pour le destinataire
            double recipientReceive = amount - fees;

            // Afficher les résultats dans les TextView correspondants
            feesTextView.setText("Frais : " + fees);
            recipientReceiveTextView.setText("Le destinataire recevra : " + recipientReceive);
        } else {
            // Réinitialiser les TextView si aucun montant n'est saisi
            feesTextView.setText("Frais :");
            recipientReceiveTextView.setText("Le destinataire recevra :");
        }
    }

    private void saveTransactionToFirebase() {
        String senderNumber = senderNumberEditText.getText().toString().trim();
        String receiverNumber = receiverNumberEditText.getText().toString().trim();
        String amountStr = amountToSendEditText.getText().toString().trim();

        double amount = Double.parseDouble(amountStr);

        // Calculer les frais (2% du montant)
        double fees = amount * (FEES_PERCENTAGE / 100.0);

        // Calculer le montant restant pour le destinataire
        double recipientReceive = amount - fees;

        // Création d'un nouvel objet Transaction
        Transaction transaction = new Transaction(senderNumber, receiverNumber, amount, fees, recipientReceive);

        // Récupérer la date et l'heure actuelles
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        // Ajouter la date et l'heure à la transaction
        transaction.setTransactionDate(currentDate);
        transaction.setTransactionTime(currentTime);

        // Associez l'ID de l'utilisateur actuel à cette transaction
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            transaction.setUserId(currentUserId);
        }

        // Enregistrement de la transaction dans la base de données Firebase
        transactionRef.push().setValue(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChoiceOperators.this, "👍", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChoiceOperators.this, "Erreur lors de l'enregistrement de la transaction", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveTransactionToHistory() {
        String senderNumber = senderNumberEditText.getText().toString().trim();
        String receiverNumber = receiverNumberEditText.getText().toString().trim();
        String amountStr = amountToSendEditText.getText().toString().trim();

        double amount = Double.parseDouble(amountStr);

        // Calculer les frais (2% du montant)
        double fees = amount * (FEES_PERCENTAGE / 100.0);

        // Calculer le montant restant pour le destinataire
        double recipientReceive = amount - fees;

        // Création d'un nouvel objet Transaction
        Transaction transaction = new Transaction(senderNumber, receiverNumber, amount, fees, recipientReceive);

        // Récupérer la date et l'heure actuelles
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        // Ajouter la date et l'heure à la transaction
        transaction.setTransactionDate(currentDate);
        transaction.setTransactionTime(currentTime);

        // Associez l'ID de l'utilisateur actuel à cette transaction
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            transaction.setUserId(currentUserId);
        }


        // Enregistrement de la transaction dans la table "History" de la base de données Firebase
        historyRef.push().setValue(transaction)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ChoiceOperators.this, "👍", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChoiceOperators.this, "Erreur lors de l'enregistrement de la transaction dans l'historique", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Gestionnaire de sélection d'un opérateur dans les Spinners
        ImageView operatorImageView;
        if (parent.getId() == R.id.send_Spinner) {
            operatorImageView = findViewById(R.id.sendImageIds);
        } else {
            operatorImageView = findViewById(R.id.receiveImageIds2);
        }
        operatorImageView.setImageResource(operatorImages[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Ne rien faire lorsque rien n'est sélectionné
    }
}
