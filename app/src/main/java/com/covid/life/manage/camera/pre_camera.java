package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;

import org.opencv.android.OpenCVLoader;

public class pre_camera extends AppCompatActivity {

    Button btnIrTemperatura;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_camera);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(this,"OpenCV esta cargado correctamente", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Erro al cargar OpenCV... :(", Toast.LENGTH_LONG).show();
        }

        btnIrTemperatura = findViewById(R.id.btnIrTemperatura);
        btnIrTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent formularioLayout = new Intent( v.getContext() , MainActivity.class);
                //startActivity(formularioLayout);
            }
        });



    }
}
