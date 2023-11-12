package com.example.transfertme;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Faq extends AppCompatActivity {

    private ImageView historyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        // Récupère les références des vues
        final TextView question1 = findViewById(R.id.question1);
        historyBtn = findViewById(R.id.ImageView0165);
        final TextView answer1 = findViewById(R.id.answer1);
        final TextView question2 = findViewById(R.id.question2);
        final TextView answer2 = findViewById(R.id.answer2);

        // Configuration du bouton d'historique pour retourner à la page de parametre
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Faq.this, SettingPage.class);
                startActivity(intent);
            }
        });

        // Associez un gestionnaire de clic à chaque question pour afficher ou masquer la réponse.
        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer1.getVisibility() == View.GONE) {
                    answer1.setVisibility(View.VISIBLE);
                } else {
                    answer1.setVisibility(View.GONE);
                }
            }
        });

        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer2.getVisibility() == View.GONE) {
                    answer2.setVisibility(View.VISIBLE);
                } else {
                    answer2.setVisibility(View.GONE);
                }
            }
        });
    }
}
