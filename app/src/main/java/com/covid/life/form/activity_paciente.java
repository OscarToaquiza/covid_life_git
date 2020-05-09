package com.covid.life.form;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.covid.life.menu.menu_pacientes;
import com.covid.life.models.Canton;
import com.covid.life.models.Paciente;
import com.covid.life.models.Provincia;
import com.covid.life.models.Seguimiento;
import com.covid.life.notificaciones.NotificacionFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activity_paciente extends AppCompatActivity {
    private Button btnGuardar;
    private CardView cvAntecedentes;
    private Spinner sGenero, sProvincia, sAislamiento, sPresion, sDiabetes, sFumador, sCancer, sDiscapacidad, sEmbarazada, sLactar, sOrganizaciones, sCantones,sParroquias;
    private DatePicker dtFechaNacimiento;
    private String[] datos;
    private TextView txtTelefono, txtDireccion, txtEnfermedad, txtAlergia, txtCerco;
    private Paciente paciente;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private String uid;
    private ArrayList<String> otros= new ArrayList<String>();


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paciente = new Paciente();
        datos = getIntent().getExtras().getStringArray("datos");

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        otros.add("otros");
        btnGuardar = findViewById(R.id.guardar);
        cvAntecedentes = (CardView) findViewById(R.id.antecedentes);
        sGenero = findViewById(R.id.spnGenero);
        dtFechaNacimiento = findViewById(R.id.fechaNacimiento);
        txtTelefono = findViewById(R.id.telefono);
        sProvincia = findViewById(R.id.spnProvincias);
        sCantones = findViewById(R.id.spnCantones);
        sParroquias = findViewById(R.id.spnParroquias);
        txtDireccion = findViewById(R.id.direccion);
        sOrganizaciones = findViewById(R.id.spnOrganizacion);
        sAislamiento = findViewById(R.id.spnAislamiento);
        sPresion = findViewById(R.id.spnPresion);
        sDiabetes = findViewById(R.id.spnDiabetes);
        sFumador = findViewById(R.id.spnFumador);
        sCancer = findViewById(R.id.spnCancer);
        sEmbarazada = findViewById(R.id.spnEmbarazada);
        sLactar = findViewById(R.id.spnLactar);
        sDiscapacidad = findViewById(R.id.spnDiscapacidad);
        txtEnfermedad = findViewById(R.id.enfermedad);
        txtAlergia = findViewById(R.id.alergia);
        txtCerco = findViewById(R.id.cerco);
        progressbar = findViewById(R.id.progressBarPaciente);


        cargarOrganizacion();
        cargarProvincia();
        cargarCanton();
        sGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrarAntecedentes(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    guardarPaciente();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }


    private void mostrarAntecedentes(int position) {
        if (position == 1 || position == 3) {
            cvAntecedentes.setVisibility(View.VISIBLE);
        } else {
            cvAntecedentes.setVisibility(View.GONE);
        }
    }

    private void guardarPaciente() throws IOException {
        obtenerDatos();
        if (obtenerSpinners() == FALSE)
            return;
        if (obtenerText() == FALSE)
            return;
        obtenerFecha();

        //obtener latitud y longitud del paciente
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            Toast.makeText(getApplicationContext(),
                    "Necesitamos permisos de ubicación !!", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        Location location = locationManager.getLastKnownLocation( locationManager.GPS_PROVIDER);
        if(location == null)
             location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location == null){
            AlertActivarGps();
            return;
        }
        double latitude = location.getLatitude();
        double longitud = location.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> list = geocoder.getFromLocation(
                location.getLatitude(), location.getLongitude(), 1);
        if (!list.isEmpty()) {
            Address DirCalle = list.get(0);
            paciente.setDireccionGPS(DirCalle.getAddressLine(0));
        }

        paciente.setLatitud(String.valueOf(latitude));
        paciente.setLongitud(String.valueOf(longitud));
        paciente.setToken(NotificacionFirebase.getToken(this));

        progressbar.setVisibility(View.VISIBLE);
            db.collection("paciente")
                    .document(uid)
                    .set(paciente)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),
                                        "Bienvenido !!", Toast.LENGTH_SHORT)
                                        .show();
                                finish();
                                Intent intent
                                        = new Intent(getApplicationContext(),
                                        menu_pacientes.class);
                                startActivity(intent);
                            }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),
                                    "Error: "+e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                            progressbar.setVisibility(View.GONE);
                            return;
                        }
                    });


    }
    private void obtenerDatos(){
        paciente.setCedula(datos[0]);
        paciente.setNombres(datos[1]);
        paciente.setApellidos(datos[2]);
        paciente.setCorreo(datos[3]);
    }

    private  boolean obtenerText(){
        if (TextUtils.isEmpty(txtTelefono.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su  número de teléfono ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }

        if (TextUtils.isEmpty(txtDireccion.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese su direccion ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }

        if (TextUtils.isEmpty(txtEnfermedad.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese la enfermedad, en caso de no tener ingrese: \"NINGUNA\" ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }

        if (TextUtils.isEmpty(txtAlergia.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese el medicamento, en caso de no tener ingrese: \"NINGUNA\" ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }

        if (TextUtils.isEmpty(txtCerco.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese el numero de familiares, en caso de no tener ingrese: \"0\" ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }
        paciente.setAlergia_medicamentos(txtAlergia.getText().toString().trim());
        paciente.setFamiliares_cerco(Integer.valueOf(txtCerco.getText().toString().trim()));
        paciente.setTiene_diagnosticado_enfermedad(txtEnfermedad.getText().toString().trim());
        paciente.setDireccion(txtDireccion.getText().toString().trim());
        paciente.setTelefono(txtTelefono.getText().toString().trim());
        return TRUE;
    }

    private void obtenerFecha(){
        Calendar calendar = new GregorianCalendar(dtFechaNacimiento.getYear(), dtFechaNacimiento.getMonth(), dtFechaNacimiento.getDayOfMonth());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date fechaNacimiento = new Date(calendar.getTimeInMillis());
        Date fechaCreacion = new Date();
        paciente.setFechaNacimiento(fechaNacimiento);
        paciente.setFecha_creacion(fechaCreacion);
    }
    private Boolean obtenerSpinners(){
        String seleccione = "-- Seleccione --";

        if(sOrganizaciones.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su Organización ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            paciente.setOrganizacion(sOrganizaciones.getSelectedItem().toString());
        }

        if(sProvincia.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su provincia", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            paciente.setProvincia(sProvincia.getSelectedItem().toString());
        }

        if(sCantones.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su Cantón", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            paciente.setCanton(sCantones.getSelectedItem().toString());
        }

        if(sParroquias.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su Parroquia", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            paciente.setParroquia(sParroquias.getSelectedItem().toString());
        }

        if(sAislamiento.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su tipo de aislamiento", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            paciente.setAislado_por(sAislamiento.getSelectedItem().toString());
        }

        if(sGenero.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione su género ", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            paciente.setGenero(sGenero.getSelectedItem().toString());
        }


        if(sPresion.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione si sufre de presión arterial alta", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            if(sPresion.getSelectedItem().toString().trim().equals("Si"))
                paciente.setTiene_presion_alta(TRUE);
            else
                paciente.setTiene_presion_alta(FALSE);
        }

        if(sDiabetes.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione si sufre de diabetes", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            if(sDiabetes.getSelectedItem().toString().trim().equals("Si"))
                paciente.setTiene_diabetes(TRUE);
            else
                paciente.setTiene_diabetes(FALSE);
        }

        if(sFumador.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione si es o fue fumador", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            if(sFumador.getSelectedItem().toString().trim().equals("Si"))
                paciente.setFue_es_fumador(TRUE);
            else
                paciente.setFue_es_fumador(FALSE);
        }

        if(sCancer.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione si le han diagnosticado cancer", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            if(sCancer.getSelectedItem().toString().trim().equals("Si"))
                paciente.setEs_diagnosticado_cancer(TRUE);
            else
                paciente.setEs_diagnosticado_cancer(FALSE);
        }

        if(sDiscapacidad.getSelectedItem().toString().trim().equals(seleccione)){
            Toast.makeText(getApplicationContext(),
                    "Seleccione si tiene carnet de discapacidad", Toast.LENGTH_LONG)
                    .show();
            return FALSE;
        }else{
            if(sDiscapacidad.getSelectedItem().toString().trim().equals("Si"))
                paciente.setTiene_carnet_discapacidad(TRUE);
            else
                paciente.setTiene_carnet_discapacidad(FALSE);
        }

        if(sGenero.getSelectedItem().toString().trim().equals("Hombre")){
            paciente.setEs_embarazada(FALSE);
            paciente.setEsta_dando_lactar(FALSE);
        }else{
            if(sEmbarazada.getSelectedItem().toString().trim().equals(seleccione)){
                Toast.makeText(getApplicationContext(),
                        "Seleccione si esta embarazada", Toast.LENGTH_LONG)
                        .show();
                return FALSE;
            }else{
                if(sEmbarazada.getSelectedItem().toString().trim().equals("Si"))
                    paciente.setEs_embarazada(TRUE);
                else
                    paciente.setEs_embarazada(FALSE);
            }

            if(sLactar.getSelectedItem().toString().trim().equals(seleccione)){
                Toast.makeText(getApplicationContext(),
                        "Seleccione si esta dando de lactar", Toast.LENGTH_LONG)
                        .show();
                return FALSE;
            }else{
                if(sLactar.getSelectedItem().toString().trim().equals("Si"))
                    paciente.setEsta_dando_lactar(TRUE);
                else
                    paciente.setEsta_dando_lactar(FALSE);
            }
        }


        return TRUE;
    }

    public void AlertActivarGps(){
        new AlertDialog.Builder(activity_paciente.this)
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

    public void cargarOrganizacion(){
        db.collection("organizacion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> organizaciones = new ArrayList<>();
                            organizaciones.add("-- Seleccione --");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                organizaciones.add(document.getData().get("nombre").toString());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, organizaciones);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sOrganizaciones.setAdapter(adapter);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void cargarProvincia(){
        db.collection("provincia")
                .orderBy("posicion",Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<Provincia> provincias = new ArrayList<>();
                            List<String> provinciasList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Provincia provincia = document.toObject(Provincia.class);
                                provincias.add(provincia);
                                provinciasList.add(provincia.getNombre());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_item, provinciasList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sProvincia.setAdapter(adapter);
                            sProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if(provincias!=null){
                                    for(Provincia provincia: provincias){
                                        if(provincia.getPosicion() == position)
                                            if(provincia.getCantones()!= null){
                                                mostrarCanton(provincia.getCantones());
                                                return;
                                            }else
                                                mostrarCanton(otros);
                                        else
                                            mostrarCanton(otros);
                                    }
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void mostrarCanton(ArrayList <String> cantones){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, cantones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCantones.setAdapter(adapter);
    }

    public void cargarCanton(){
        db.collection("canton")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final List<Canton> cantones = new ArrayList<>();
                            List<String> cantonesList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Canton canton = document.toObject(Canton.class);
                                cantones.add(canton);
                                cantonesList.add(canton.getNombre());
                            }

                            sCantones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    if(cantones!=null){
                                        for(Canton canton: cantones){
                                            Log.d("canton",parent.getSelectedItem().toString());
                                            if(parent.getSelectedItem().toString().trim().equals(canton.getNombre())){
                                                if(canton.getParroquias()!= null){
                                                    mostrarParroquia(canton.getParroquias());
                                                    return;
                                                }else
                                                    mostrarParroquia(otros);
                                            }else
                                                if(canton.getParroquias()!= null)
                                                    mostrarParroquia(otros);
                                        }
                                    }
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void mostrarParroquia(ArrayList <String> parroquias){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_item, parroquias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sParroquias.setAdapter(adapter);
    }
 }
