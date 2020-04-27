package com.covid.life.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.covid.life.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class activity_recuperar extends AppCompatActivity {
    private EditText emailTextView ;
    private Button Btn;
    private ProgressBar progressbar;

    private FirebaseAuth mAuth;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // taking instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        emailTextView = findViewById(R.id.email);
        Btn = findViewById(R.id.recuperar);
        progressbar = findViewById(R.id.progressBar);

        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                recuperarContrasena();
            }
        });
    }

    private void recuperarContrasena(){
        progressbar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = emailTextView.getText().toString().trim();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su email !!",
                    Toast.LENGTH_LONG)
                    .show();
            progressbar.setVisibility(View.GONE);
            return;
        }

        Btn.setEnabled(false);
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity_recuperar.this, "Le hemos enviado instrucciones para restablecer su contraseña", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity_recuperar.this, "Error al enviar el correo electrónico de restablecimiento !", Toast.LENGTH_SHORT).show();
                        }

                        progressbar.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Btn.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                "Error: "+e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }


}
