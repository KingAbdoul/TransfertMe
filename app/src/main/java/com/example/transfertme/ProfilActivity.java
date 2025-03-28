package com.example.transfertme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
/** IMPORTS FIRESTORE **/
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
/** IMPORTS STORAGE **/
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 100;

    private ImageView profile_cancel, profileImage, changePhotoIcon;
    private TextView usernameText, emailText;
    private EditText editFullName, signup_phone;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference userDoc;
    private StorageReference storageRef;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // 1. Vérifier si l'utilisateur est connecté
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            finish();
            return;
        }

        // 2. Récupérer l'UID et init Firestore
        String userId = currentUser.getUid();
        db = FirebaseFirestore.getInstance();
        userDoc = db.collection("users").document(userId);

        // 3. Init Storage pour la photo de profil
        storageRef = FirebaseStorage.getInstance().getReference("profileImages").child(userId);

        // 4. Récupérer les vues
        profile_cancel = findViewById(R.id.profile_cancel);
        profileImage = findViewById(R.id.profileImage);
        changePhotoIcon = findViewById(R.id.changePhotoIcon);
        usernameText = findViewById(R.id.usernameText);
        emailText = findViewById(R.id.emailText);
        editFullName = findViewById(R.id.editFullName);
        signup_phone = findViewById(R.id.signup_phone);
        saveButton = findViewById(R.id.saveButton);


        profile_cancel.setOnClickListener(v -> finish());

        // 6. Charger les infos depuis Firestore
        loadUserInfo();

        // 7. Changer la photo de profil
        changePhotoIcon.setOnClickListener(v -> openImageChooser());

        // 8. Enregistrer modifications
        saveButton.setOnClickListener(v -> saveUserInfo());
    }

    private void loadUserInfo() {
        // Lire le document Firestore
        userDoc.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Récupérer les champs
                String dbName = documentSnapshot.getString("name");
                String dbEmail = documentSnapshot.getString("email");
                String dbPhone = documentSnapshot.getString("phone");

                // Afficher dans usernameText, emailText
                if (dbName != null) usernameText.setText(dbName);
                if (dbEmail != null) emailText.setText(dbEmail);

                // Mettre dans editFullName, signup_phone
                editFullName.setText(dbName);
                signup_phone.setText(dbPhone);
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(ProfilActivity.this, "Erreur de chargement du profil.", Toast.LENGTH_SHORT).show();
        });

        // Charger l'image depuis Storage (si existante)
        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                   Picasso.get().load(uri).into(profileImage);
                })
                .addOnFailureListener(e -> {
                });
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choisir une image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                // Afficher l'aperçu dans profileImage
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserInfo() {
        // Récupérer les valeurs modifiées
        String newName = editFullName.getText().toString().trim();
        String newPhone = signup_phone.getText().toString().trim();

        // Mettre à jour Firestore
        userDoc.update("name", newName);
        userDoc.update("phone", newPhone);

        // Mettre à jour l'affichage du haut
        usernameText.setText(newName);

        // Si l'utilisateur a sélectionné une nouvelle photo
        if (imageUri != null) {
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // recharger l'image
                    })
                    .addOnFailureListener(e -> {

                    });
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserInfo();
    }

}
