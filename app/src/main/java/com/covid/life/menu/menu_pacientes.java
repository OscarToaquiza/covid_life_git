package com.covid.life.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.form.activity_guia;
import com.covid.life.notificaciones.activity_llamada;
import com.google.firebase.auth.FirebaseAuth;

public class menu_pacientes extends AppCompatActivity {
    private Button btnGuia, btnSignosVitales, btnEmergencias;
    private FirebaseAuth mAuth;
    private int contador=0;
    private String correo,contrasena;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_pacientes);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cargarPreferencias();

        mAuth = FirebaseAuth.getInstance();
        btnGuia = findViewById(R.id.btnGuia);
        btnSignosVitales = findViewById(R.id.btnSignosVitales);
        btnEmergencias = findViewById(R.id.btnEmergencia);

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

        btnEmergencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dial = "tel:911";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(contador == 0 && correo.equals("")){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu:
                mostrarDialogo();
                break;

            case R.id.videollamada:
                mostrarLlamada();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarLlamada(){
        SharedPreferences preferences = getSharedPreferences("notificacion", Context.MODE_PRIVATE);
        String enlace = preferences.getString("enlace","");
        Intent intent = new Intent(getApplicationContext(),
                activity_llamada.class);
        intent.putExtra("enlace",enlace);
        startActivity(intent);
    }
    public void mostrarDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(menu_pacientes.this);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("Desea cerrar sesión ?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(),"Sesión cerrada",Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    borrarPreferencias();
                    finish();
                }
            })
            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
    }

    public void borrarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email","");
        editor.putString("password","");
        editor.commit();
    }

    public void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        correo = preferences.getString("email","");
        contrasena = preferences.getString("password","");
    }


}
