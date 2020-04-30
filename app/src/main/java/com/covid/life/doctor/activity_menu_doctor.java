package com.covid.life.doctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.menu.menu_pacientes;
import com.covid.life.notificaciones.NotificacionFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_menu_doctor extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private int contador=0;
    private String correo,contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_doctor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        cargarPreferencias();
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
        inflater.inflate(R.menu.opciones,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu:
                mostrarDialogo();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void mostrarDialogo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_menu_doctor.this);
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
