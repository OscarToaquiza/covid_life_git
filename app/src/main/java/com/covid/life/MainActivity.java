/*
*
* ESTE MENSAJE SE ENCUENTRA EN LA RAMA  "DESARROLLO"
* LA RAMA MASTER SE LLAMARÁ "PRODUCCION"
* DONDE ESTE MENSAJE NO CONSTA
*
*
* DESARROLLO_SIGNOS VITALES
* Oscar Toaquiza.
* */

package com.covid.life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.covid.life.manage.camera.pre_camera;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Pry inicial 01/04/2020
        // PRY A PRESENTAR
        Button btnIniciar = (Button) findViewById(R.id.btnInit);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inicioLayout = new Intent( v.getContext() , pre_camera.class);
                inicioLayout.putExtra("Puntos",new ArrayList<Double>());
                startActivity(inicioLayout );
            }
        });
    }
}
