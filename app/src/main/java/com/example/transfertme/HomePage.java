package com.example.transfertme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    private ImageView signOutBtn;
    private ImageView homeBtn;
    private ImageView settingBtn;
    private ImageView notificationBtn;
    private ImageView historyBtn;
    private TextView signup_name;
    private ImageView actionButtonSend;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Récupérer les références des vues
        signup_name = findViewById(R.id.signup_name);
        signOutBtn = findViewById(R.id.imageView3);
        homeBtn = findViewById(R.id.imageView7);
        notificationBtn = findViewById(R.id.imageView8);
        historyBtn = findViewById(R.id.imageView10);
        settingBtn = findViewById(R.id.imageView4);
        actionButtonSend = findViewById(R.id.ActionButtonsend);

        // Configurer le bouton de déconnexion
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        // Configurer le bouton de retour à la page d'accueil
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        // Configurer le bouton de notification
        notificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, NotificationPage.class);
                startActivity(intent);
            }
        });

        // Configurer le bouton d'historique
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, HistoryPage.class);
                startActivity(intent);
            }
        });

        // Configurer le bouton de paramètres
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, SettingPage.class);
                startActivity(intent);
            }
        });

        // Configurer le bouton ActionButtonSend pour rediriger vers ActivityChoiceOperators
        actionButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePage.this, ChoiceOperators.class);
                startActivity(intent);
            }
        });

        // Obtenir le nom de l'utilisateur connecté
        getLoggedInUserName();
    }

    // Méthode pour obtenir le nom de l'utilisateur connecté
    private void getLoggedInUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        HelperClass currentUser = snapshot.getValue(HelperClass.class);
                        if (currentUser != null) {
                            String name = currentUser.getName();
                            signup_name.setText(name);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gérer les erreurs d'annulation de la base de données
                }
            });
        }
    }
}
