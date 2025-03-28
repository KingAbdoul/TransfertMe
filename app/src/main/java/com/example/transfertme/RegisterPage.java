package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterPage extends AppCompatActivity {


    private TextInputEditText signupName, signupEmail, signupPassword, signupPhone;
    private Button signupButton;
    private TextView signinRedirectText;


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //  Initialiser Firebase (si pas déjà fait dans l'Application)
        FirebaseApp.initializeApp(this);

        // Initialiser l'authentification Firebase
        mAuth = FirebaseAuth.getInstance();

        // Initialiser Firestore
        db = FirebaseFirestore.getInstance();
        userRef = db.collection("users");

        // Récupérer les vues
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPassword = findViewById(R.id.signup_password);
        signupPhone = findViewById(R.id.signup_phone);
        signupButton = findViewById(R.id.signup_button);
        signinRedirectText = findViewById(R.id.signin_redirectText);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });


        signinRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Vérifie les champs, vérifie si l'email est déjà utilisé,
     * puis crée l'utilisateur dans Firebase Auth si tout va bien.
     */
    private void createAccount() {
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();
        String phone = signupPhone.getText().toString().trim();

        // Vérifier que tous les champs sont remplis
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(password) || TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterPage.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier la longueur du mot de passe
        if (password.length() < 6) {
            Toast.makeText(RegisterPage.this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
            return;
        }

        // Vérifier si l'e-mail est déjà utilisé
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                        if (isNewUser) {
                            registerNewUser(name, email, password, phone);
                        } else {
                            Toast.makeText(RegisterPage.this, "Cette adresse e-mail est déjà utilisée. " +
                                    "Veuillez utiliser une autre adresse ou vous connecter.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterPage.this, "Erreur lors de la vérification de l'adresse e-mail. " +
                                "Veuillez réessayer.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Enregistre un nouvel utilisateur dans FirebaseAuth + Firestore.
     */
    private void registerNewUser(String name, String email, String password, String phone) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Compte créé avec succès
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Construire l'objet HelperClass
                            HelperClass helper = new HelperClass(name, email, password, phone);


                            // Enregistrer dans Firestore
                            String userId = user.getUid();
                            userRef.document(userId).set(helper)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            // Afficher un message de confirmation
                                            Toast.makeText(RegisterPage.this,
                                                    "Votre compte a été créé avec succès !",
                                                    Toast.LENGTH_LONG).show();

                                            // Envoyer un e-mail de vérification
                                            sendVerificationEmail(user);

                                            // Vider les champs
                                            clearFields();

                                            // Rediriger l'utilisateur vers MainActivity (ou rester ici)
                                            startActivity(new Intent(RegisterPage.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterPage.this,
                                                    "Erreur lors de l'enregistrement en base (Firestore). " +
                                                            "Veuillez réessayer.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        // Gérer les exceptions spécifiques
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(RegisterPage.this,
                                    "Cette adresse e-mail est déjà utilisée. " +
                                            "Veuillez utiliser une autre adresse ou vous connecter.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RegisterPage.this,
                                    "Erreur lors de l'inscription. " +
                                            "Vérifiez votre connexion ou réessayez plus tard.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * (Optionnel) Envoie un e-mail de vérification à l'utilisateur.
     */
    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterPage.this,
                                "Un e-mail de vérification a été envoyé à " + user.getEmail(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(RegisterPage.this,
                                "Impossible d'envoyer l'e-mail de vérification. Réessayez plus tard.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Vide les champs de saisie
     */
    private void clearFields() {
        signupName.setText("");
        signupEmail.setText("");
        signupPassword.setText("");
        signupPhone.setText("");
    }
}
