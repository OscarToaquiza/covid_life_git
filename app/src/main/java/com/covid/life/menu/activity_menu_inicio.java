package com.covid.life.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.form.activity_signosVitales;
import com.google.firebase.auth.FirebaseAuth;

public class activity_menu_inicio extends AppCompatActivity {
    private Button btnAgregar, btnCerrarSesion;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        mAuth = FirebaseAuth.getInstance();
        btnAgregar = findViewById(R.id.Agregar);
        btnCerrarSesion = findViewById(R.id.cerrrarSesion);
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        activity_signosVitales.class);
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mAuth.getCurrentUser().getEmail();
                Toast.makeText(getApplicationContext(),
                        email.toString(), Toast.LENGTH_SHORT).show();
                //mAuth.signOut();
            }
        });
    }
}
