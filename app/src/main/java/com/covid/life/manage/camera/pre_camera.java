package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.covid.life.R;

import org.opencv.android.OpenCVLoader;

public class pre_camera extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_camera);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(this,"OpenCV esta cargado correctamente", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Erro al cargar OpenCV... :(", Toast.LENGTH_LONG).show();
        }

    }
}
