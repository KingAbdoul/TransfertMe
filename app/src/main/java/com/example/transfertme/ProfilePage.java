package com.example.transfertme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ProfilePage extends AppCompatActivity {

    TextView profileName, profileEmail, profileUsername, profileBirth;
    TextView titleName, titleUsername;
    Button editProfile;
    private ImageView settingsProfile, profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profileBirth = findViewById(R.id.profileBirth);
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfile = findViewById(R.id.editButton);
        settingsProfile = findViewById(R.id.settingsProfile);
        profileImg = findViewById(R.id.profileImg);

        showUserData();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });

        settingsProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfilePage.this, SettingPage.class));
            }
        });
    }

    public void showUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        if (currentUser != null) {
            String userId = currentUser.getUid();

            reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nameUser = snapshot.child("name").getValue(String.class);
                        String emailUser = snapshot.child("email").getValue(String.class);
                        String usernameUser = snapshot.child("username").getValue(String.class);
                        String birthUser = snapshot.child("birth").getValue(String.class);
                        String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                        titleName.setText(nameUser);
                        titleUsername.setText(usernameUser);
                        profileName.setText(nameUser);
                        profileEmail.setText(emailUser);
                        profileBirth.setText(birthUser);
                        profileUsername.setText(usernameUser);

                        // Charger et afficher la photo de profil de l'utilisateur en utilisant Picasso
                        Picasso.get().load(profileImageUrl).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                // Transformer l'image en image de profil rectangle avec coins arrondis
                                Bitmap roundedBitmap = ImageUtils.getRoundedRectBitmap(bitmap);
                                profileImg.setImageBitmap(roundedBitmap);
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
                public void onCancelled(@NonNull DatabaseError error) {
                    // Gérer les erreurs de base de données
                }
            });
        }
    }

    public void passUserData() {
        String userUsername = profileUsername.getText().toString().trim();

        Intent intent = new Intent(ProfilePage.this, EditProfile.class);
        intent.putExtra("username", userUsername);
        startActivity(intent);
    }
}
