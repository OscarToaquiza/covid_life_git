package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.covid.life.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

public class pre_camera extends AppCompatActivity {

    Button btnIrTemperatura;

    private  ArrayList<Double> puntos;
    private GraphView mScatterPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_camera);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(this,"OpenCV esta cargado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Erro al cargar OpenCV... :(", Toast.LENGTH_LONG).show();
        }
        mScatterPlot = findViewById(R.id.grafica);
        btnIrTemperatura = findViewById(R.id.btnIrTemperatura);
        btnIrTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formularioLayout = new Intent( v.getContext() , camera_data.class);
                startActivity(formularioLayout);
            }
        });

        puntos =  (ArrayList<Double>) getIntent().getSerializableExtra("Puntos");


        if(puntos.size() > 0){
            init();
            Log.d("TamanioVecotr",""+puntos.size());
        }

    }


    private  void init(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Double i = 0.0;
        mScatterPlot.getViewport().setScalable(true);
        mScatterPlot.getViewport().setMinY(0);
        mScatterPlot.getViewport().setMaxY(300);
        mScatterPlot.getViewport().setXAxisBoundsManual(true);

        for( Double valorY: puntos  ){
            series.appendData(new DataPoint( i , valorY   ),true,1000);
            i++;
        }
        mScatterPlot.addSeries(series);

    }
}
