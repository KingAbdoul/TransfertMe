package com.example.transfertme;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/** IMPORT FIRESTORE **/
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private ImageView dot1, dot2, dot3;
    private TextView signupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // 1. Initialiser Firebase
        FirebaseApp.initializeApp(this);

        // 2. Récupérer le ViewFlipper
        viewFlipper = findViewById(R.id.viewFlipper);

        // 3. Récupérer les 3 points
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        // Configuration initiale des points
        updateDots(0);

        // 4. Gérer les animations d’entrée/sortie du ViewFlipper
        if (viewFlipper.getInAnimation() != null) {
            viewFlipper.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    int displayedChild = viewFlipper.getDisplayedChild();
                    updateDots(displayedChild);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }
        if (viewFlipper.getOutAnimation() != null) {
            viewFlipper.getOutAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }
                @Override
                public void onAnimationEnd(Animation animation) {
                    int displayedChild = viewFlipper.getDisplayedChild();
                    updateDots(displayedChild);
                }
                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }

        // 5. Récupérer le TextView où afficher le nom
        signupName = findViewById(R.id.signup_name);

        // 6. Charger le nom depuis Firestore
        fetchAndDisplayUserName();

        // 7. Icône Paramètres
        LinearLayout paramIcon = findViewById(R.id.leftContainer1);
        if (paramIcon != null) {
            paramIcon.setOnClickListener(v -> {
                ParametresBottomSheet bottomSheet = new ParametresBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "ParametresBottomSheet");
            });
        }

        // 8. Profil
        LinearLayout profilRedirectLayout = findViewById(R.id.profilredirectText);
        if (profilRedirectLayout != null) {
            profilRedirectLayout.setOnClickListener(v -> {
                startActivity(new Intent(HomePage.this, ProfilActivity.class));
            });
        }

        // 9. Transaction
        LinearLayout transactionLayout = findViewById(R.id.linearlayout);
        if (transactionLayout != null) {
            transactionLayout.setOnClickListener(v -> {
                startActivity(new Intent(HomePage.this, TransactionActivity.class));
            });
        }

        // 10. Historiques
        LinearLayout rightContainer = findViewById(R.id.rightContainer);
        if (rightContainer != null) {
            rightContainer.setOnClickListener(v -> {
                // Ouvrir HistoriquesActivity
                startActivity(new Intent(HomePage.this, HistoriquesActivity.class));
            });
        }
    }

    /**
     * Met à jour l’état (visuel) des 3 points en fonction de l’index (0, 1, 2).
     */
    private void updateDots(int index) {
        dot1.setImageResource(R.drawable.dot_unselected);
        dot2.setImageResource(R.drawable.dot_unselected);
        dot3.setImageResource(R.drawable.dot_unselected);

        switch (index) {
            case 0:
                dot1.setImageResource(R.drawable.dot_selected);
                break;
            case 1:
                dot2.setImageResource(R.drawable.dot_selected);
                break;
            case 2:
                dot3.setImageResource(R.drawable.dot_selected);
                break;
        }
    }


    private void fetchAndDisplayUserName() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            signupName.setText("Bienvenue, Inconnu");
            return;
        }

        String userId = currentUser.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(userId);

        docRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("name");
                        if (userName != null) {
                            signupName.setText("Bienvenue, " + userName);
                        } else {
                            signupName.setText("Bienvenue, Inconnu");
                        }
                    } else {
                        signupName.setText("Bienvenue, Inconnu");
                    }
                })
                .addOnFailureListener(e -> {
                    signupName.setText("Erreur de lecture");
                });
    }

    /**
     * Réactualise le nom
     */
    @Override
    protected void onResume() {
        super.onResume();
        fetchAndDisplayUserName();
    }
}
