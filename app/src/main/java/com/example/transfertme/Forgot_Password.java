package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Forgot_Password extends AppCompatActivity {

    private TextInputEditText forgotEmail;
    private Button addForgot;
    private TextView loginRedirectText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // 1. Initialiser Firebase
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        // 2. Récupérer les vues
        forgotEmail = findViewById(R.id.forgot_email);
        addForgot = findViewById(R.id.Add_forgot);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        // 3. Gérer le clic sur le bouton Réinitialiser
        addForgot.setOnClickListener(view -> {
            String email = forgotEmail.getText().toString().trim();

            // Vérifier si le champ est vide
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Forgot_Password.this,
                        "Veuillez saisir votre adresse e-mail",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Envoyer l'e-mail de réinitialisation du mot de passe
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            Toast.makeText(Forgot_Password.this,
                                    "Un e-mail de réinitialisation a été envoyé à " + email,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            // Gérer les exceptions
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {

                                Toast.makeText(Forgot_Password.this,
                                        "Aucun compte associé à cet e-mail. " +
                                                "Veuillez vérifier l'adresse saisie.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Forgot_Password.this,
                                        "Erreur lors de l'envoi de l'e-mail de réinitialisation. " +
                                                "Veuillez réessayer.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        // 4. Redirection "S'identifier"
        loginRedirectText.setOnClickListener(v -> {
            // Rediriger vers MainActivity
            startActivity(new Intent(Forgot_Password.this, MainActivity.class));
            finish(); // Empêche le retour en arrière
        });
    }
}
