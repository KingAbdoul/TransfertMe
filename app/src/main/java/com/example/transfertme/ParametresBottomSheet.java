package com.example.transfertme;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;

public class ParametresBottomSheet extends BottomSheetDialogFragment {

    private ImageView exitIcon;
    private LinearLayout optionProfil;
    private LinearLayout optionContact;
    private LinearLayout optionFaq;
    private LinearLayout optionModifierMdp;
    private LinearLayout optionAnnulerTransfert;
    private Button optionDeconnexion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Appliquer un style si nécessaire
        setStyle(STYLE_NORMAL, R.style.TransparentStatusBarBottomSheetTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // Empêcher la fermeture en cliquant hors du dialogue
        dialog.setCanceledOnTouchOutside(false);

        //  Supprimer l’assombrissement
        if (dialog.getWindow() != null) {
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Gonfle le layout parametres_bottom_sheet.xml
        View view = inflater.inflate(R.layout.parametres_bottom_sheet, container, false);

        // 1. Récupérer les vues
        exitIcon = view.findViewById(R.id.exit_con);
        optionProfil = view.findViewById(R.id.optionProfi);
        optionContact = view.findViewById(R.id.optionContac);
        optionFaq = view.findViewById(R.id.optionFa);
        optionModifierMdp = view.findViewById(R.id.optionModifierMd);
        optionAnnulerTransfert = view.findViewById(R.id.optionAnnulerTransfer);
        optionDeconnexion = view.findViewById(R.id.optionDeconnexio);


        exitIcon.setOnClickListener(v -> dismiss());


        // (Option Profil)
        optionProfil.setOnClickListener(v -> {
            dismiss();  // Fermer la bottom sheet
            startActivity(new Intent(requireContext(), ProfilActivity.class));
        });

        // (Option Contactez-nous)
        optionContact.setOnClickListener(v -> {

        });

        // => (Option FAQ)
        optionFaq.setOnClickListener(v -> {

        });

        // => (Option Modifier mot de passe) : ICI, on redirige vers HistoriquesActivity
        optionModifierMdp.setOnClickListener(v -> {
            dismiss();  // Fermer la bottom sheet
            // Rediriger vers HistoriquesActivity
            startActivity(new Intent(requireContext(), HistoriquesActivity.class));
        });

        // (Option Annuler un transfert)
        optionAnnulerTransfert.setOnClickListener(v -> {

        });

        // 4. Bouton "optionDeconnexion" : déconnecter l'utilisateur
        optionDeconnexion.setOnClickListener(v -> {

            FirebaseAuth.getInstance().signOut();

            dismiss();

            // (Optionnel) Rediriger vers MainActivity
            startActivity(new Intent(getContext(), MainActivity.class));
            requireActivity().finish();
        });

        return view;
    }
}
