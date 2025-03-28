package com.example.transfertme;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TransactionActivity extends AppCompatActivity {

    private ImageView closeIcon;


    private LinearLayout sendOrangeLine, sendMtnLine;
    private ImageView sendOrangeRadio, sendMtnRadio;

    private LinearLayout receiveOrangeLine, receiveMtnLine;
    private ImageView receiveOrangeRadio, receiveMtnRadio;

    private EditText senderNumberEdit, receiverNumberEdit, amountEdit;
    private TextView feesLabel, receiverGetsLabel;
    private Button sendButton;

    private static final double FEES_PERCENT = 0.02;


    private String currentSendOperator = "ORANGE";
    private String currentReceiveOperator = "MTN";

    // Référence Firestore
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        // 1. Initialiser Firestore et vérifier l'utilisateur
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // 2. Récupérer les vues
        closeIcon = findViewById(R.id.closeIcon);


        sendOrangeLine = findViewById(R.id.sendOrangeLine);
        sendMtnLine = findViewById(R.id.sendMtnLine);
        sendOrangeRadio = findViewById(R.id.sendOrangeRadio);
        sendMtnRadio = findViewById(R.id.sendMtnRadio);


        receiveOrangeLine = findViewById(R.id.receiveOrangeLine);
        receiveMtnLine = findViewById(R.id.receiveMtnLine);
        receiveOrangeRadio = findViewById(R.id.receiveOrangeRadio);
        receiveMtnRadio = findViewById(R.id.receiveMtnRadio);


        senderNumberEdit = findViewById(R.id.senderNumberEdit);
        receiverNumberEdit = findViewById(R.id.receiverNumberEdit);
        amountEdit = findViewById(R.id.amountEdit);

        feesLabel = findViewById(R.id.feesLabel);
        receiverGetsLabel = findViewById(R.id.receiverGetsLabel);
        sendButton = findViewById(R.id.sendButton);


        closeIcon.setOnClickListener(v -> finish());

        // 4. Gestion du bloc ENVOI
        sendOrangeLine.setOnClickListener(v -> selectSendOperator(true));
        sendMtnLine.setOnClickListener(v -> selectSendOperator(false));

        // 5. Gestion du bloc RECEPTION
        receiveOrangeLine.setOnClickListener(v -> selectReceiveOperator(true));
        receiveMtnLine.setOnClickListener(v -> selectReceiveOperator(false));

        // 6. Surveiller le champ "Montant à envoyer" pour recalculer les frais
        amountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                updateFeesAndReceiverGets();
            }
        });


        sendButton.setOnClickListener(v -> processTransaction());

        // Initialiser par défaut (Orange en envoi, MTN en réception)
        selectSendOperator(true);
        selectReceiveOperator(false);
    }

    private void selectSendOperator(boolean isOrange) {
        if (isOrange) {
            sendOrangeRadio.setImageResource(R.drawable.ic_radio_checked);
            sendMtnRadio.setImageResource(R.drawable.ic_radio_unchecked);
            currentSendOperator = "ORANGE";
            Toast.makeText(this, "Vous avez sélectionné ORANGE pour l'envoi", Toast.LENGTH_SHORT).show();
        } else {
            sendOrangeRadio.setImageResource(R.drawable.ic_radio_unchecked);
            sendMtnRadio.setImageResource(R.drawable.ic_radio_checked);
            currentSendOperator = "MTN";
            Toast.makeText(this, "Vous avez sélectionné MTN pour l'envoi", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sélectionne l’opérateur en réception (Orange ou Mtn).
     */
    private void selectReceiveOperator(boolean isOrange) {
        if (isOrange) {
            receiveOrangeRadio.setImageResource(R.drawable.ic_radio_checked);
            receiveMtnRadio.setImageResource(R.drawable.ic_radio_unchecked);
            currentReceiveOperator = "ORANGE";
            Toast.makeText(this, "Vous avez sélectionné ORANGE pour la réception", Toast.LENGTH_SHORT).show();
        } else {
            receiveOrangeRadio.setImageResource(R.drawable.ic_radio_unchecked);
            receiveMtnRadio.setImageResource(R.drawable.ic_radio_checked);
            currentReceiveOperator = "MTN";
            Toast.makeText(this, "Vous avez sélectionné MTN pour la réception", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Met à jour l’affichage des frais et du montant que recevra le destinataire.
     */
    private void updateFeesAndReceiverGets() {
        String amountStr = amountEdit.getText().toString().trim();
        if (!amountStr.isEmpty()) {
            try {
                double amount = Double.parseDouble(amountStr);
                // Calcul des frais 2%
                double fees = amount * FEES_PERCENT;
                // Montant final
                double receiverGets = amount - fees;

                feesLabel.setText(String.format(Locale.getDefault(),
                        "Frais: 2%% (=%.0f)", fees));
                receiverGetsLabel.setText(String.format(Locale.getDefault(),
                        "Le destinataire recevra : %.0f", receiverGets));
            } catch (NumberFormatException e) {
                feesLabel.setText("Frais: 2%");
                receiverGetsLabel.setText("Le destinataire recevra : 0");
            }
        } else {
            // Champs vide => on remet par défaut
            feesLabel.setText("Frais: 2%");
            receiverGetsLabel.setText("Le destinataire recevra : 0");
        }
    }

    /**
     * Lance la logique d’envoi : validation, enregistrement dans Firestore, etc.
     */
    private void processTransaction() {
        if (currentUser == null) {
            Toast.makeText(this, "Vous n'êtes pas connecté(e).", Toast.LENGTH_SHORT).show();
            return;
        }

        // Récupérer les valeurs
        String sender = senderNumberEdit.getText().toString().trim();
        String receiver = receiverNumberEdit.getText().toString().trim();
        String amountStr = amountEdit.getText().toString().trim();

        // Vérifier que tous les champs ne sont pas vides
        if (sender.isEmpty() || receiver.isEmpty() || amountStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier si c’est multiple de 100
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Montant invalide", Toast.LENGTH_SHORT).show();
            return;
        }
        if (amount % 100 != 0) {
            Toast.makeText(this, "Le montant doit être un multiple de 100", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculer les frais et le montant final
        double fees = amount * FEES_PERCENT;
        double receiverGets = amount - fees;

        // Préparer les données à stocker
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("senderOperator", currentSendOperator);
        transactionData.put("receiverOperator", currentReceiveOperator);
        transactionData.put("senderNumber", sender);
        transactionData.put("receiverNumber", receiver);
        transactionData.put("amount", amount);
        transactionData.put("fees", fees);
        transactionData.put("receiverGets", receiverGets);

        // Ajouter la date/heure
        String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        transactionData.put("dateTime", dateTime);

        // Enregistrer dans Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference transactionsRef = db
                .collection("users")
                .document(currentUser.getUid())
                .collection("transactions");

        // Ajouter un document auto-généré
        transactionsRef.add(transactionData)
                .addOnSuccessListener(documentReference -> {
                    // Succès
                    Toast.makeText(this, "Transaction enregistrée avec succès !", Toast.LENGTH_LONG).show();
                    finish(); // Fermer l'activité
                })
                .addOnFailureListener(e -> {
                    // Erreur
                    Toast.makeText(this, "Erreur lors de l'enregistrement : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
