package com.covid.life.form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.menu.activity_menu_inicio;
import com.covid.life.models.Seguimiento;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import static java.lang.Boolean.TRUE;

public class activity_signosVitales extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView txtTemperatura, txtFrecuencia,txtSaturacion;
    private Button btnAgregar;
    private ProgressBar progressBar;
    private Seguimiento signosVitales;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signos_vitales);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        signosVitales = new Seguimiento();
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        signosVitales.setIdPaciente(uid);
        signosVitales.setCreadoporPaciente(TRUE);

        txtTemperatura = findViewById(R.id.temperatura);
        txtFrecuencia = findViewById(R.id.frecuenciaCardiaca);
        txtSaturacion = findViewById(R.id.saturacionOxigeno);
        btnAgregar = findViewById(R.id.RegistrarSignos);
        progressBar = findViewById(R.id.progressBar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSignosVitales();
            }
        });
    }

    private void guardarSignosVitales(){
        if (TextUtils.isEmpty(txtTemperatura.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese la temperatura ", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(txtFrecuencia.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese la frecuencia cardíaca ", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(txtSaturacion.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese la saturación de oxígeno ", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        signosVitales.setSat_oxigeno(Double.parseDouble(txtSaturacion.getText().toString().trim()));
        signosVitales.setRitmo_cardiaco(Double.parseDouble(txtFrecuencia.getText().toString().trim()));
        signosVitales.setTemperatura(Double.parseDouble(txtTemperatura.getText().toString().trim()));

        Date fecha = new Date();
        signosVitales.setFecha(fecha);

        progressBar.setVisibility(View.VISIBLE);
        btnAgregar.setEnabled(false);
        db.collection("seguimiento")
                .add(signosVitales)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),
                                "Signos Vitales Agregados !!", Toast.LENGTH_SHORT)
                                .show();
                        progressBar.setVisibility(View.GONE);
                        finish();
                        Intent intent
                                = new Intent(getApplicationContext(),
                                activity_menu_inicio.class);
                        startActivity(intent);



                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        btnAgregar.setEnabled(true);
                        Toast.makeText(getApplicationContext(),
                                "Error: "+e.getMessage(), Toast.LENGTH_SHORT)
                                .show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
