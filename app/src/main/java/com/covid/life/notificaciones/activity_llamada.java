package com.covid.life.notificaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.models.Notificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class activity_llamada extends AppCompatActivity {
    private TextView txtEnlacellamada;
    private ProgressBar pbLlamada;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String token = "";

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        txtEnlacellamada = findViewById(R.id.tvEnlaceLlamada);
        pbLlamada = findViewById(R.id.progressBarLlamada);
        buscarUltimaNotificacion();

    }

    public void buscarUltimaNotificacion(){
        token = NotificacionFirebase.getToken(this);
        pbLlamada.setVisibility(View.VISIBLE);
        db.collection("notificacion")
                .whereEqualTo("token", token)
                .orderBy("fecha", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            pbLlamada.setVisibility(View.GONE);
                            txtEnlacellamada.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Notificacion notificacion = document.toObject(Notificacion.class);
                                txtEnlacellamada.setText(notificacion.getUrl());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
