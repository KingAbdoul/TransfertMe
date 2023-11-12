package com.example.transfertme;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;

    private ImageView ImageBack;
    private EditText editName, editUsername, editBirth;
    private Button saveButton;
    private ImageView profileImg;

    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ImageBack = findViewById(R.id.ImageBack);
        editName = findViewById(R.id.editName);
        editUsername = findViewById(R.id.editUsername);
        editBirth = findViewById(R.id.editBirth);
        saveButton = findViewById(R.id.saveButton);
        profileImg = findViewById(R.id.profileImg);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentUserID = currentUser.getUid();
            userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

            ImageBack.setOnClickListener(v -> finish());

            saveButton.setOnClickListener(v -> saveUserProfile());

            profileImg.setOnClickListener(v -> {
                // Vérifier si l'autorisation de lecture du stockage externe est accordée
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Demander l'autorisation de lecture du stockage externe
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
                } else {
                    // L'autorisation est déjà accordée, on peut effectuer les actions nécessaires
                    // liées à la lecture du stockage externe ici
                    performActionsAfterPermissionGranted();
                }
            });
        } else {
            // Gérer le cas où l'utilisateur n'est pas connecté
            // Par exemple, rediriger vers l'écran de connexion
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void saveUserProfile() {
        String name = editName.getText().toString().trim();
        String username = editUsername.getText().toString().trim();
        String birth = editBirth.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editName.setError("Veuillez entrer votre nom");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            editUsername.setError("Veuillez entrer votre nom d'utilisateur");
            return;
        }

        if (TextUtils.isEmpty(birth)) {
            editBirth.setError("Veuillez entrer votre date de naissance");
            return;
        }

        // Mettre à jour les informations de l'utilisateur dans la base de données
        userRef.child("name").setValue(name);
        userRef.child("username").setValue(username);
        userRef.child("birth").setValue(birth);

        Toast.makeText(this, "Informations de profil mises à jour", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void performActionsAfterPermissionGranted() {
        // Ouvrir la galerie pour sélectionner une image
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'autorisation de lecture du stockage externe a été accordée
                performActionsAfterPermissionGranted();
            } else {
                // L'autorisation de lecture du stockage externe a été refusée
                // Gérer cette situation en conséquence (par exemple, afficher un message d'erreur)
                Toast.makeText(this, "Autorisation refusée. Impossible de lire le stockage externe.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            // Récupérer l'URI de l'image sélectionnée depuis la galerie
            Uri imageUri = data.getData();

            // Générer un nom unique pour l'image en utilisant UUID
            String imageName = UUID.randomUUID().toString();

            // Référence de stockage Firebase Storage pour l'image
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile_images").child(imageName);

            // Télécharger l'image vers Firebase Storage
            storageReference.putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                // L'image a été téléchargée avec succès
                                // Récupérer l'URL de téléchargement de l'image
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        // Mettre à jour l'URL de la photo de profil dans la base de données
                                        userRef.child("profileImageUrl").setValue(imageUrl);

                                        // Charger l'image dans ImageView avec coins arrondis
                                        Picasso.get().load(imageUrl).into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                // Appliquer les coins arrondis à l'image de profil
                                                Bitmap roundedBitmap = ImageUtils.getRoundedRectBitmap(bitmap);
                                                profileImg.setImageBitmap(roundedBitmap);
                                            }

                                            @Override
                                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                // Gérer l'échec du chargement de l'image
                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                // Préparation du chargement de l'image
                                            }
                                        });

                                        Toast.makeText(EditProfile.this, "Image téléchargée avec succès", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Échec de récupération de l'URL de téléchargement de l'image
                                        Toast.makeText(EditProfile.this, "Erreur lors du téléchargement de l'image", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                // Échec du téléchargement de l'image
                                Toast.makeText(EditProfile.this, "Erreur lors du téléchargement de l'image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void showDatePickerDialog(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Traitement de la date sélectionnée
                String date = dayOfMonth + "/" + (month + 1) + "/"  + year;
                editBirth.setText(date);
            }
        }, 2000, 0, 1); // Valeurs par défaut pour l'année, le mois et le jour
        datePickerDialog.show();
    }
}
