package com.covid.life.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.form.activity_signosVitales;
import com.covid.life.models.Seguimiento;
import com.covid.life.resultados.activity_resultado;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class activity_menu_inicio extends AppCompatActivity {
    private Button btnAgregar, btnCerrarSesion;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Seguimiento> signosVitalesList;
    private ProgressBar progressbar;
    private int contador;
    private TextView mensaje;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        btnAgregar = findViewById(R.id.Agregar);
        progressbar = findViewById(R.id.progressBar);
        signosVitalesList = new ArrayList<Seguimiento>() ;
        mensaje = findViewById(R.id.smsSignos);

        obtenerSignosVitales();
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validarPermisos()==true){
                    finish();
                    Intent intent = new Intent(getApplicationContext(),
                            activity_signosVitales.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void obtenerSignosVitales(){
        progressbar.setVisibility(View.VISIBLE);

        db.collection("seguimiento")
                .whereEqualTo("idPaciente", mAuth.getUid())
                .orderBy("fecha", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            progressbar.setVisibility(View.GONE);
                            int cont = 0;
                            Date fechaActual= new Date();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Seguimiento seguimiento = document.toObject(Seguimiento.class);
                                Date fecha = seguimiento.getFecha();
                                String fechaPac = convertString(fecha);
                                String fechaAct = convertString(fechaActual);
                                if(fechaPac.equals(fechaAct)){
                                    cont++;
                                }

                             }

                            if(cont == 3){
                                mensaje.setText("Ya no puedes agregar mas signos vitales por hoy dia !!");
                            }
                            if(cont < 3)
                                btnAgregar.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public String convertString(Date fecha){
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaStr = formatoFecha.format(fecha);

        return fechaStr;
    }

    private boolean validarPermisos(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Toast.makeText(getApplicationContext(),
                    "Necesitamos permisos de ubicación !!", Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        Location location = locationManager.getLastKnownLocation( locationManager.GPS_PROVIDER);
        if(location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location == null){
            AlertActivarGps();
            return false;
        }
        return true;
    }

    public void AlertActivarGps(){
        new AlertDialog.Builder(activity_menu_inicio.this)
                .setIcon(R.drawable.ic_report_problem)
                .setTitle("Ubicación")
                .setMessage("Para un mejor resultado esta aplicacion necesita que la ubicación este activada.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                }).show();
    }

}
