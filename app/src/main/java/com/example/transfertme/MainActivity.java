package com.example.transfertme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private CheckBox rememberMe;
    private Button loginButton;
    private TextView signupRedirectText;
    private TextView emailEditText;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        rememberMe = findViewById(R.id.remember_me);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        emailEditText = findViewById(R.id.forgotRedirectText);

        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        boolean rememberChecked = sharedPreferences.getBoolean("rememberChecked", false);
        rememberMe.setChecked(rememberChecked);
        if (rememberChecked) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedPassword = sharedPreferences.getString("password", "");
            loginEmail.setText(savedEmail);
            loginPassword.setText(savedPassword);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();
                final boolean remember = rememberMe.isChecked();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Utiliser Firebase Authentication pour l'authentification de l'utilisateur
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Authentification réussie
                                    if (remember) {
                                        // Enregistrer les informations d'identification dans les préférences partagées
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", email);
                                        editor.putString("password", password);
                                        editor.putBoolean("rememberChecked", true);
                                        editor.apply();
                                    } else {
                                        // Supprimer les informations d'identification des préférences partagées
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.remove("email");
                                        editor.remove("password");
                                        editor.remove("rememberChecked");
                                        editor.apply();
                                    }
                                    // Démarrer l'activité suivante après une connexion réussie
                                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                                    startActivity(intent);
                                    finish(); // Fermer l'activité en cours pour éviter de revenir en arrière
                                } else {
                                    // Échec de l'authentification
                                    Toast.makeText(MainActivity.this, "Authentification invalide", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la page d'inscription
                Intent intent = new Intent(MainActivity.this, RegisterPage.class);
                startActivity(intent);
            }
        });

        emailEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ouvrir la page d'inscription
                Intent intent = new Intent(MainActivity.this, Forgot_Password.class);
                startActivity(intent);
            }
        });
            }

    }

