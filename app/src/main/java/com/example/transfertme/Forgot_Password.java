package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password extends AppCompatActivity {

    private EditText emailEditText;
    private Button sendButton;
    private TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Récupérer les références des vues à partir des identifiants XML
        emailEditText = findViewById(R.id.forgot_email);
        sendButton = findViewById(R.id.Add_forgot);
        signupRedirectText = findViewById(R.id.loginRedirectText);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer l'adresse e-mail entrée par l'utilisateur
                String email = emailEditText.getText().toString().trim();

                if (email.isEmpty()) {
                    // Afficher un message d'erreur si l'adresse e-mail est vide
                    Toast.makeText(Forgot_Password.this, "Veuillez entrer votre adresse e-mail", Toast.LENGTH_SHORT).show();
                } else {
                    // Appeler la méthode pour réinitialiser le mot de passe avec l'adresse e-mail
                    resetPassword(email);
                }
            }
        });

    signupRedirectText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void  onClick(View v) {
            // Ouvrir la page d'inscription
            Intent intent = new Intent(Forgot_Password.this, MainActivity.class);
            startActivity(intent);
        }
    });
    }

    private void resetPassword(String email) {
        // Obtenir une instance de FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Envoyer l'e-mail de réinitialisation du mot de passe à l'adresse e-mail fournie
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Afficher un message de succès si l'e-mail a été envoyé avec succès
                            Toast.makeText(Forgot_Password.this, "Un e-mail de réinitialisation du mot de passe a été envoyé à votre adresse e-mail.", Toast.LENGTH_SHORT).show();

                            // Redirection vers la classe MainActivity
                            Intent intent = new Intent(Forgot_Password.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Afficher un message d'erreur si l'e-mail de réinitialisation n'a pas pu être envoyé
                            Toast.makeText(Forgot_Password.this, "Échec de l'envoi de l'e-mail de réinitialisation du mot de passe. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
