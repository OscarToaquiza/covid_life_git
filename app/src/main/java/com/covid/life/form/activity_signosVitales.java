package com.covid.life.form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activity_signosVitales extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView txtTemperatura, txtFrecuencia,txtSaturacion,txtEstadoActual;
    private Button btnAgregar;
    private ProgressBar progressBar;
    private Seguimiento signosVitales;
    private Spinner estado;
    private double latitude,longitud ;



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
        signosVitales.setEstado_alta(null);
        signosVitales.setToma_muestra(null);
        signosVitales.setTipo(null);
        signosVitales.setDificultad_respirar(null);
        signosVitales.setRequerimiento(null);


        txtTemperatura = findViewById(R.id.temperatura);
        txtFrecuencia = findViewById(R.id.frecuenciaCardiaca);
        txtSaturacion = findViewById(R.id.saturacionOxigeno);
        btnAgregar = findViewById(R.id.RegistrarSignos);
        progressBar = findViewById(R.id.progressBar);
        txtEstadoActual = findViewById(R.id.estadoActual);
        estado = findViewById(R.id.spnEstado);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSignosVitales();
            }
        });
    }

    private void guardarSignosVitales(){
        if (TextUtils.isEmpty(txtTemperatura.getText().toString().trim())) {
            txtTemperatura.setError("Ingrese la temperatura");
            return;
        }

        if (TextUtils.isEmpty(txtFrecuencia.getText().toString().trim())) {
            signosVitales.setRitmo_cardiaco(0.0);
        }else
            signosVitales.setRitmo_cardiaco(Double.parseDouble(txtFrecuencia.getText().toString().trim()));

        if (TextUtils.isEmpty(txtSaturacion.getText().toString().trim())) {
            signosVitales.setSat_oxigeno(0.0);
        }else
            signosVitales.setSat_oxigeno(Double.parseDouble(txtSaturacion.getText().toString().trim()));

        if (TextUtils.isEmpty(txtEstadoActual.getText().toString().trim())) {
            txtEstadoActual.setError("Describa su estado actual de salud");
            return;
        }


        String seleccione = "-- Seleccione --";

        if(estado.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su estado", Toast.LENGTH_LONG)
                    .show();
            return;
        }else{
            switch (estado.getSelectedItem().toString()){
                case "Estoy Empeorando":
                    signosVitales.setEstadoSalud("DETERIORO");
                    break;
                case "Sigo Estable":
                    signosVitales.setEstadoSalud("BUENA_EVOLUCION");
                    break;
                case "Mejoría de síntomas":
                    signosVitales.setEstadoSalud("IGUAL_EVOLUCION");
                    break;
            }

        }



        signosVitales.setTemperatura(Double.parseDouble(txtTemperatura.getText().toString().trim()));
        signosVitales.setEstadoActual(txtEstadoActual.getText().toString().trim());

        Date fecha = new Date();
        signosVitales.setFecha(fecha);

        if(obtenerLocalizacion()!=true)
            return;

        progressBar.setVisibility(View.VISIBLE);
        btnAgregar.setEnabled(false);
        db.collection("seguimiento")
                .add(signosVitales)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        progressBar.setVisibility(View.GONE);
                        AlertSignosVitales();

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

    private boolean obtenerLocalizacion(){
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Toast.makeText(getApplicationContext(),
                    "Necesitamos permisos de ubicación !!", Toast.LENGTH_LONG)
                    .show();
            return false;
        }

        Location location = locationManager.getLastKnownLocation( locationManager.GPS_PROVIDER);
        if(location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location == null){
            AlertActivarGps();
            return false;
        }
        latitude = location.getLatitude();
        longitud = location.getLongitude();
        signosVitales.setLatitud(String.valueOf(latitude));
        signosVitales.setLongitud(String.valueOf(longitud));
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(
                    location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!list.isEmpty()) {
            Address DirCalle = list.get(0);
            signosVitales.setDireccion(DirCalle.getAddressLine(0));
        }
        return true;
    }

    public void AlertActivarGps(){
        new AlertDialog.Builder(activity_signosVitales.this)
                .setIcon(R.drawable.ic_report_problem)
                .setTitle("Ubicación")
                .setMessage("Para un mejor resultado, esta aplicacion necesita que la ubicación este activada.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                }).show();
    }

    public void AlertSignosVitales(){
        new AlertDialog.Builder(activity_signosVitales.this)
                .setIcon(R.drawable.ic_check_circle)
                .setTitle("Signos Vitales")
                .setCancelable(false)
                .setMessage(
                        "** ¡Sus signos vitales han sido ingresados con éxito!\n" +
                        "** Si es necesario nos comunicaremos contigo\n" +
                        "** ¡Recuerda!\n" +
                        "Los signos vitales se ingresan a las 6H00, 12h00 y 18H00.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Intent intent
                                = new Intent(getApplicationContext(),
                                activity_menu_inicio.class);
                        startActivity(intent);
                    }
                }).show();
    }
}
