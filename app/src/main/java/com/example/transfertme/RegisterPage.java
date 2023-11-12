package com.example.transfertme;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterPage extends AppCompatActivity {

    // Les variables pour les EditText, TextView et Button
    EditText signupName, signupEmail, signupUsername, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    EditText signupBirth;

    // Les variables Firebase
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Initialise Firebase
        FirebaseApp.initializeApp(this);

        // Initialise les références des vues
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupBirth = findViewById(R.id.signup_birth);

        // Initialise l'instance de la base de données Firebase
        database = FirebaseDatabase.getInstance();
        // Initialise la référence à la table "users" dans la base de données
        reference = database.getReference("users");

        // Initialise l'instance de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configure le bouton de création de compte
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Récupère les valeurs des champs de saisie
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String birth = signupBirth.getText().toString();
                String username = signupUsername.getText().toString();
                String password = signupPassword.getText().toString();

                // Vérifie si tous les champs sont renseignés
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(birth)
                        || TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterPage.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Crée une instance de la classe HelperClass avec les données de l'utilisateur
                HelperClass helperClass = new HelperClass(name, email, birth, username, password);

                // Utilise Firebase Authentication pour créer un nouvel utilisateur avec l'adresse e-mail et le mot de passe
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // L'utilisateur a été créé avec succès, enregistre les données de l'utilisateur dans la base de données Firebase
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        String userId = user.getUid();
                                        reference.child(userId).setValue(helperClass);

                                        // Envoie un e-mail à l'utilisateur avec ses informations de connexion
                                        sendEmailVerification(user);

                                        // Affiche un message de succès
                                        Toast.makeText(RegisterPage.this, "Vous vous êtes inscrit avec succès!", Toast.LENGTH_SHORT).show();

                                        // Redirige l'utilisateur vers l'Activity MainActivity
                                        Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                } else {
                                    // Une erreur s'est produite lors de la création de l'utilisateur, affiche un message d'erreur
                                    Toast.makeText(RegisterPage.this, "Erreur lors de l'inscription. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Configure le texte de redirection vers la page de connexion
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirige l'utilisateur vers l'Activity MainActivity
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Configure l'EditText de la date de naissance pour afficher le sélecteur de date
        signupBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    public void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Traitement de la date sélectionnée
                String formattedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                signupBirth.setText(formattedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // L'e-mail de vérification a été envoyé avec succès
                            Toast.makeText(RegisterPage.this, "Un e-mail de vérification a été envoyé à votre adresse e-mail. Veuillez vérifier votre boîte de réception.", Toast.LENGTH_LONG).show();
                        } else {
                            // Une erreur s'est produite lors de l'envoi de l'e-mail de vérification
                            Toast.makeText(RegisterPage.this, "Erreur lors de l'envoi de l'e-mail de vérification. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
