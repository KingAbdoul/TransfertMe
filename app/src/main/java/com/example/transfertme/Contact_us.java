package com.example.transfertme;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class Contact_us extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Button openWhatsAppButton = findViewById(R.id.openWhatsAppButton);
        ImageView imageView0165 = findViewById(R.id.ImageView0165);

        openWhatsAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsAppLink("https://wa.me/message/TUDFS4GI3AJYC1");
            }
        });

        // Gérer le clic sur ImageView0165
        imageView0165.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Rediriger vers la page SettingPage
                Intent intent = new Intent(Contact_us.this, SettingPage.class);
                startActivity(intent);
                finish(); // Fermer la page actuelle
            }
        });
    }

    private void openWhatsAppLink(String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(link));
            startActivity(intent);
        } catch (Exception e) {
            // Gérer les erreurs, par exemple si WhatsApp n'est pas installé sur l'appareil
        }
    }
}
