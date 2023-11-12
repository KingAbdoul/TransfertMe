package com.example.transfertme;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class OTPVerification extends AppCompatActivity {

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                if (selectedETPosition == 0) {
                    selectedETPosition = 1;
                    showKeyboard(otpEt2);
                } else if (selectedETPosition == 1) {
                    selectedETPosition = 2;
                    showKeyboard(otpEt3);
                } else if (selectedETPosition == 2) {
                    selectedETPosition = 3;
                    showKeyboard(otpEt4);
                }
            }
        }
    };

    private EditText otpEt1, otpEt2, otpEt3, otpEt4;
    private TextView resendBtn;

    private boolean resendEnabled = false;
    private int resendTime = 60;

    private int selectedETPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        otpEt1 = findViewById(R.id.otpET1);
        otpEt2 = findViewById(R.id.otpET2);
        otpEt3 = findViewById(R.id.otpET3);
        otpEt4 = findViewById(R.id.otpET4);
        resendBtn = findViewById(R.id.resendBtn);
        final Button verifyBtn = findViewById(R.id.verifyBtn);


        otpEt1.addTextChangedListener(textWatcher);
        otpEt2.addTextChangedListener(textWatcher);
        otpEt3.addTextChangedListener(textWatcher);
        otpEt4.addTextChangedListener(textWatcher);

        showKeyboard(otpEt1);

        startCountDownTimer();

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled) {
                    startCountDownTimer();
                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Vérifier si les champs sont vides
                if (otpEt1.getText().toString().isEmpty() || otpEt2.getText().toString().isEmpty()
                        || otpEt3.getText().toString().isEmpty() || otpEt4.getText().toString().isEmpty()) {
                    showEmptyFieldsMessage();
                } else {
                    // Lorsque l'utilisateur entre un code, afficher un message "Transfert effectué"
                    showSuccessMessage();
                }
            }
        });
    }

    private void showKeyboard(EditText otpET) {
        otpET.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otpET, InputMethodManager.SHOW_IMPLICIT);
    }

    private void startCountDownTimer() {
        resendEnabled = false;
        resendBtn.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Renvoyer le code (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                resendBtn.setText("Renvoyer le code");
                resendBtn.setTextColor(getResources().getColor(R.color.primary));
            }
        }.start();
    }

    private void showSuccessMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Transfert effectué")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Rediriger vers la page d'accueil (HomePage)
                        Intent intent = new Intent(OTPVerification.this, HomePage.class);
                        startActivity(intent);
                        finish(); // Optionnel : fermer cette activité
                    }
                })
                .setCancelable(false)
                .create();
        alertDialog.show();
    }

    private void showEmptyFieldsMessage() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Veuillez entrer le code a 4 chiffres que vous avez reçu par message.")
                .setPositiveButton("OK", null)
                .setCancelable(true)
                .create();
        alertDialog.show();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            if (selectedETPosition == 3) {
                selectedETPosition = 2;
                showKeyboard(otpEt3);
            } else if (selectedETPosition == 2) {
                selectedETPosition = 1;
                showKeyboard(otpEt2);
            } else if (selectedETPosition == 1) {
                selectedETPosition = 0;
                showKeyboard(otpEt1);
            }
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }
}
