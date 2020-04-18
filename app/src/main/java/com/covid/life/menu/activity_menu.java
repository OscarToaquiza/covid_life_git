package com.covid.life.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.form.activity_test;
import com.covid.life.login.activity_login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class activity_menu extends AppCompatActivity {
    private Button btnLogin,btnTest,btnWeb;
    private String url;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final int REQUEST_ACCESS_FINE=1000;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Validacion para acceder a la ubicacion
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, REQUEST_ACCESS_FINE);

        }

        btnLogin = findViewById(R.id.btnlogin);
        btnTest = findViewById(R.id.btntest);
        btnWeb = findViewById(R.id.btnPersonal);

        url = "https://covid-life.firebaseapp.com";

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarPreferencias();


            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), activity_test.class);
                startActivity(intent);
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_ACCESS_FINE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Bienvenido !!",Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(this,"Necesitamos permiso para acceder a tu ubicacion !!",Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }


    public void cargarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("credenciales", Context.MODE_PRIVATE);
        String correo = preferences.getString("email","");
        String contrasena = preferences.getString("password","");

        if(correo.equals("") && contrasena.equals("")){
            Intent intent = new Intent(getApplicationContext(), activity_login.class);
            startActivity(intent);
        }else{
            login(correo,contrasena);
        }
    }

    public void login(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Bienvenido !!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    Intent intent
                                            = new Intent(activity_menu.this,
                                            menu_pacientes.class);
                                    startActivity(intent);
                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                            "Inicio de sesión fallida !!",
                                            Toast.LENGTH_LONG)
                                            .show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Error: "+e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}
