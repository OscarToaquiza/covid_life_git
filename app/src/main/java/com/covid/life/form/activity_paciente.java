package com.covid.life.form;

import android.content.Intent;
import android.os.Bundle;

import com.covid.life.MainActivity;
import com.covid.life.login.activity_registration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.covid.life.R;

public class activity_paciente extends AppCompatActivity {
    private Button btnGuardar;
    private CardView cvAntecedentes;
    private Spinner sGenero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paciente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnGuardar = findViewById(R.id.guardar);
        cvAntecedentes = (CardView) findViewById(R.id.antecedentes);
        sGenero = findViewById(R.id.spnGenero);


        sGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    cvAntecedentes.setVisibility(View.VISIBLE);
                else
                    cvAntecedentes.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent
                        = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
