package com.example.transfertme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class SettingPage extends AppCompatActivity {

    private ImageView homePageBtn;
    private AppCompatButton ProfilPageBtn;
    private TextView signup_name;
    private ImageView profileImage;
    private ImageView shareAppBtn;
    private ImageView faqBtn; // Nouveau ImageView pour FAQ
    private ImageView helpMeBtn; // Nouveau ImageView pour l'aide
    private ImageView CancelTransferBtn;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_page);

        // Récupère les références des vues
        homePageBtn = findViewById(R.id.ImageView30);
        ProfilPageBtn = findViewById(R.id.modifprofil);
        signup_name = findViewById(R.id.signup_name);
        profileImage = findViewById(R.id.profileImg);
        shareAppBtn = findViewById(R.id.ImageViewsend);
        faqBtn = findViewById(R.id.ImageViewfaqread);
        helpMeBtn = findViewById(R.id.ImageViewhelpme);
        CancelTransferBtn = findViewById(R.id.ImageViewhelpme10);

        // Configure le bouton pour revenir à la page d'accueil
        homePageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        // Configure le bouton pour accéder à la page du profil
        ProfilPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, ProfilePage.class);
                startActivity(intent);
            }
        });

        // Configure le bouton de partage de l'application
        shareAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareAppLink();
            }
        });

        // Configure le bouton pour accéder à la FAQ
        faqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, Faq.class);
                startActivity(intent);
            }
        });

        // Configure le bouton pour accéder à l'aide
        helpMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, Contact_us.class);
                startActivity(intent);
            }
        });

        // Configure le bouton pour accéder à l'aide
        CancelTransferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingPage.this, Cancel_A_Transfer.class);
                startActivity(intent);
            }
        });

        // Appelle la méthode pour obtenir le nom de l'utilisateur connecté et afficher la photo de profil
        getLoggedInUserDetails();
    }

    // Méthode pour obtenir le nom de l'utilisateur connecté et afficher la photo de profil
    private void getLoggedInUserDetails() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                        signup_name.setText(name);

                        // Charger et afficher la photo de profil de l'utilisateur en utilisant Picasso
                        Picasso.get().load(profileImageUrl).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                // Transformer image en image de profil rectangle avec coins arrondis
                                Bitmap roundedBitmap = ImageUtils.getRoundedRectBitmap(bitmap);
                                profileImage.setImageBitmap(roundedBitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                // Action à effectuer avant le chargement de l'image (facultatif)
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Gérer les erreurs de lecture de la base de données
                }
            });
        }
    }

    // Méthode pour partager le lien de l'application
    private void shareAppLink() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "TransfertMe");
        intent.putExtra(Intent.EXTRA_TEXT, "Découvrez TransfertMe, une application de transfert d'argent facile et rapide ! Téléchargez-la ici : [insérez le lien de téléchargement de l'application]");
        startActivity(Intent.createChooser(intent, "Partager l'application"));
    }
}
