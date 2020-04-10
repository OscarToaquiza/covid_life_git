package com.covid.life.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.form.activity_guia;
import com.google.firebase.auth.FirebaseAuth;

public class menu_pacientes extends AppCompatActivity {
    Button btnGuia, btnSignosVitales;
    private int contador=0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pacientes);

        mAuth = FirebaseAuth.getInstance();
        btnGuia = findViewById(R.id.btnGuia);
        btnSignosVitales = findViewById(R.id.btnSignosVitales);

        btnSignosVitales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        activity_menu_inicio.class);
                startActivity(intent);
            }
        });

        btnGuia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        activity_guia.class);
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed(){
        if(contador == 0){
            Toast.makeText(getApplicationContext(),
                    "Presione de nuevo para cerrar sesión ", Toast.LENGTH_SHORT).show();
            contador++;
        }else{
            super.onBackPressed();
        }

        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mAuth.signOut();
                contador = 0;
            }
        }.start();
    }
}
