package com.example.transfertme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText loginEmail, loginPassword;
    private CheckBox rememberMe;
    private Button loginButton;
    private TextView signupRedirectText, forgotPasswordText;
    private ProgressDialog progressDialog;

    private static final String PREFS_NAME = "loginPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REMEMBER = "rememberChecked";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        rememberMe = findViewById(R.id.remember_me);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        // Initialisation du ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connexion en cours...");
        progressDialog.setCancelable(false);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isRememberChecked = prefs.getBoolean(KEY_REMEMBER, false);
        rememberMe.setChecked(isRememberChecked);

        if (isRememberChecked) {
            loginEmail.setText(prefs.getString(KEY_EMAIL, ""));
            loginPassword.setText(prefs.getString(KEY_PASSWORD, ""));
        }

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Affichage de l'effet de chargement
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, task -> {
                        progressDialog.dismiss(); // Masquer le chargement

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (rememberMe.isChecked()) {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putString(KEY_EMAIL, email);
                                    editor.putString(KEY_PASSWORD, password);
                                    editor.putBoolean(KEY_REMEMBER, true);
                                    editor.apply();
                                } else {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.apply();
                                }

                                startActivity(new Intent(MainActivity.this, HomePage.class));
                                finish();
                            }
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Toast.makeText(MainActivity.this, "Cet utilisateur n'existe pas. Inscrivez-vous.", Toast.LENGTH_SHORT).show();
                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this, "Mot de passe incorrect. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Échec de la connexion. Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        forgotPasswordText.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, Forgot_Password.class));
            finish();
        });

        signupRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterPage.class));
            finish();
        });
    }
}
