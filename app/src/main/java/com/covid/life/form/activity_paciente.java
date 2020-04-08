package com.covid.life.form;

import android.content.Intent;
import android.os.Bundle;

import com.covid.life.menu.activity_menu_inicio;
import com.covid.life.models.Paciente;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class activity_paciente extends AppCompatActivity {
    private Button btnGuardar;
    private CardView cvAntecedentes;
    private Spinner sGenero ,sProvincia,sAislamiento,sPresion,sDiabetes,sFumador,sCancer,sDiscapacidad ,sEmbarazada,sLactar;
    private DatePicker dtFechaNacimiento;
    private String[] datos;
    private TextView txtTelefono,txtCanton,txtDireccion,txtEnfermedad,txtAlergia,txtCerco;
    private Paciente paciente;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int validar = 0;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        paciente= new Paciente();
        datos =  getIntent().getExtras().getStringArray("datos");

        mAuth = FirebaseAuth.getInstance();
        String  udi = mAuth.getCurrentUser().getUid();
        paciente.setId(udi);

        btnGuardar = findViewById(R.id.guardar);
        cvAntecedentes = (CardView) findViewById(R.id.antecedentes);
        sGenero = findViewById(R.id.spnGenero);
        dtFechaNacimiento = findViewById(R.id.fechaNacimiento);
        txtTelefono = findViewById(R.id.telefono);
        sProvincia = findViewById(R.id.spnProvincias);
        txtCanton = findViewById(R.id.canton);
        txtDireccion = findViewById(R.id.direccion);
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
        sGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mostrarAntecedentes(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {  }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarPaciente();

            }
        });


    }



    private void mostrarAntecedentes(int position) {
        if (position == 1) {
            cvAntecedentes.setVisibility(View.VISIBLE);
        } else {
            cvAntecedentes.setVisibility(View.GONE);
        }
    }

    private void guardarPaciente(){
        obtenerDatos();
        obtenerSpinners();
        if(obtenerText()==FALSE){
            return;
        }
        obtenerFecha();
        progressbar.setVisibility(View.VISIBLE);
            db.collection("paciente")
                    .add(paciente)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(),
                                    "Bienvenido !!", Toast.LENGTH_SHORT)
                                    .show();
                            progressbar.setVisibility(View.GONE);
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
                            Toast.makeText(getApplicationContext(),
                                    "Error: "+e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                            progressbar.setVisibility(View.GONE);
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

        if (TextUtils.isEmpty(txtCanton.getText().toString().trim())) {
            Toast.makeText(getApplicationContext(),
                    "Ingrese el canton ", Toast.LENGTH_LONG)
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
        paciente.setCanton(txtCanton.getText().toString().trim());
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
    private void obtenerSpinners(){
        paciente.setGenero(sGenero.getSelectedItem().toString());
        paciente.setProvincia(sProvincia.getSelectedItem().toString());
        paciente.setAislado_por(sAislamiento.getSelectedItem().toString());

        if(sPresion.getSelectedItem().toString().trim().equals("Si"))
            paciente.setTiene_presion_alta(TRUE);
        else
            paciente.setTiene_presion_alta(FALSE);

        if(sDiabetes.getSelectedItem().toString().trim().equals("Si"))
            paciente.setTiene_diabetes(TRUE);
        else
            paciente.setTiene_diabetes(FALSE);

        if(sFumador.getSelectedItem().toString().trim().equals("Si"))
            paciente.setFue_es_fumador(TRUE);
        else
            paciente.setFue_es_fumador(FALSE);

        if(sCancer.getSelectedItem().toString().trim().equals("Si"))
            paciente.setEs_diagnosticado_cancer(TRUE);
        else
            paciente.setEs_diagnosticado_cancer(FALSE);

        if(sDiscapacidad.getSelectedItem().toString().trim().equals("Si"))
            paciente.setTiene_carnet_discapacidad(TRUE);
        else
            paciente.setTiene_carnet_discapacidad(FALSE);

        if(sEmbarazada.getSelectedItem().toString().trim().equals("Si"))
            paciente.setEs_embarazada(TRUE);
        else
            paciente.setEs_embarazada(FALSE);

        if(sLactar.getSelectedItem().toString().trim().equals("Si"))
            paciente.setEsta_dando_lactar(TRUE);
        else
            paciente.setEsta_dando_lactar(FALSE);
    }

    /*private void obtenerSpinners() {
        if(sGenero.getSelectedItemPosition()!=0){
            paciente.setGenero(sGenero.getSelectedItem().toString());
            validar = 1;
        }
        else
            validar=0;

        if(sProvincia.getSelectedItemPosition() != 0){
            paciente.setProvincia(sProvincia.getSelectedItem().toString());
            validar=1;
        }else
            validar=0;

        if(sAislamiento.getSelectedItemPosition() != 0){
            paciente.setAislado_por(sAislamiento.getSelectedItem().toString());
            validar=1;
        }else
            validar=0;

        if(sPresion.getSelectedItemPosition() != 0){

            if(sPresion.getSelectedItem().toString().trim().equals("Si"))
                paciente.setTiene_presion_alta(TRUE);
            else
                paciente.setTiene_presion_alta(FALSE);
            validar=1;
        }else
            validar=0;

        if(sDiabetes.getSelectedItemPosition() != 0){
            if(sDiabetes.getSelectedItem().toString().trim().equals("Si"))
                paciente.setTiene_diabetes(TRUE);
            else
                paciente.setTiene_diabetes(FALSE);
            validar=1;
        }else
            validar=0;

        if(sFumador.getSelectedItemPosition() != 0){
            if(sFumador.getSelectedItem().toString().trim().equals("Si"))
                paciente.setFue_es_fumador(TRUE);
            else
                paciente.setFue_es_fumador(FALSE);
            validar=1;
        }else
            validar=0;

        if( sCancer.getSelectedItemPosition() != 0){
            if(sCancer.getSelectedItem().toString().trim().equals("Si"))
                paciente.setEs_diagnosticado_cancer(TRUE);
            else
                paciente.setEs_diagnosticado_cancer(FALSE);
            validar=1;
        }else
            validar=0;

        if(sDiscapacidad.getSelectedItemPosition() != 0){
            if(sDiscapacidad.getSelectedItem().toString().trim().equals("Si"))
                paciente.setTiene_carnet_discapacidad(TRUE);
            else
                paciente.setTiene_carnet_discapacidad(FALSE);
            validar=1;
        }else
            validar=0;

        if( sEmbarazada.getSelectedItemPosition() != 0){
            if(sEmbarazada.getSelectedItem().toString().trim().equals("Si"))
                paciente.setEs_embarazada(TRUE);
            else
                paciente.setEs_embarazada(FALSE);
            validar=1;
        }else
            validar=0;

        if(sLactar.getSelectedItemPosition() != 0){
            if(sLactar.getSelectedItem().toString().trim().equals("Si"))
                paciente.setEsta_dando_lactar(TRUE);
            else
                paciente.setEsta_dando_lactar(FALSE);
            validar=1;
        }else
            validar=0;

    }*/
}
