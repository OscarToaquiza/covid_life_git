package com.covid.life.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.form.activity_signosVitales;
import com.covid.life.models.Seguimiento;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
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
    private int contador=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicio);
        mAuth = FirebaseAuth.getInstance();
        btnAgregar = findViewById(R.id.Agregar);
        btnCerrarSesion = findViewById(R.id.cerrrarSesion);
        signosVitalesList = new ArrayList<Seguimiento>() ;


        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),
                        activity_signosVitales.class);
                startActivity(intent);
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //mAuth.signOut();
            }
        });
    }

    public void ObtenerSignosVitales(){

        //ArrayList<Seguimiento> seguimientoList = new ArrayList<>();

        db.collection("seguimiento")
                .whereEqualTo("idPaciente", mAuth.getUid())
                .limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Seguimiento seguimiento = document.toObject(Seguimiento.class);
                                signosVitalesList.add(seguimiento);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //Toast.makeText(getApplicationContext(), String.valueOf(signosVitalesList.size()), Toast.LENGTH_SHORT).show();
        //return seguimientoList;
    }

    public void SignosVitalesDiario(){
        Date fechaActual = new  Date();
        DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaAct = formatoFecha.format(fechaActual);
        ObtenerSignosVitales();
        for(Seguimiento signosVitales : signosVitalesList){
            /*String fechaBD = formatoFecha.format(signosVitales.getFecha());
            if(!fechaAct.equals(fechaBD)){
                sVitalesList.remove(signosVitales);
            }*/
        }

        //Toast.makeText(getApplicationContext(),
          //      String.valueOf(sVitalesList.size()), Toast.LENGTH_SHORT).show();

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
