package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.apache.commons.math3.complex.Complex;
import org.jtransforms.fft.DoubleFFT_1D;
import org.opencv.android.OpenCVLoader;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class pre_camera extends AppCompatActivity {

    Button btnIrTemperatura;

    private  ArrayList<Double> puntos;
    private GraphView mScatterPlot,mScatterPlot2;
    private long seconds;
    private  TextView txtRitmoCardiacoP, txtParrafoRitmoCardiaco;
    private ImageView imgRitnoCardico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_camera);

        if(OpenCVLoader.initDebug()){
            Toast.makeText(this,"OpenCV esta cargado correctamente", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Erro al cargar OpenCV... :(", Toast.LENGTH_LONG).show();
        }

        txtRitmoCardiacoP = findViewById(R.id.ritmoCardiaco);
        mScatterPlot = findViewById(R.id.grafica);
        mScatterPlot2 = findViewById(R.id.graficaFourier);
        btnIrTemperatura = findViewById(R.id.btnIrTemperatura);

        txtParrafoRitmoCardiaco = findViewById(R.id.txt_parrafo_ritmo_cardiaco);
        imgRitnoCardico = findViewById(R.id.img_ritmo_cardiaco);

        btnIrTemperatura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formularioLayout = new Intent( v.getContext() , camera_data.class);
                startActivity(formularioLayout);
            }
        });

        puntos =  (ArrayList<Double>) getIntent().getSerializableExtra("Puntos");
        seconds =   getIntent().getLongExtra("Segundos",0);

        if(puntos.size() > 0){
            //Ocultar Img y Parrafo
            imgRitnoCardico.setVisibility(View.GONE);
            txtParrafoRitmoCardiaco.setVisibility(View.GONE);
            //Mostrar Grafias
            mScatterPlot.setVisibility(View.VISIBLE);
            mScatterPlot2.setVisibility(View.VISIBLE);

            for( int w = 0; w < puntos.size(); w++){
                Log.d("Vector R"+w,""+puntos.get(w));
            }

            graficaUno();
            graficaDos(puntos,seconds);
        }else{
            //Mostrar Img y Parrafo
            imgRitnoCardico.setVisibility(View.VISIBLE);
            txtParrafoRitmoCardiaco.setVisibility(View.VISIBLE);
            //Ocultar Grafias
            mScatterPlot.setVisibility(View.GONE);
            mScatterPlot2.setVisibility(View.GONE);
        }
    }


    private  void graficaUno(){
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Double i = 0.0;
        mScatterPlot.setTitle("Variación Canal Rojo");
        mScatterPlot.getViewport().setScalable(true);
        mScatterPlot.getViewport().setMinY(0);
        mScatterPlot.getViewport().setMaxY(300);
        mScatterPlot.getViewport().setXAxisBoundsManual(true);
        mScatterPlot.getGridLabelRenderer().setTextSize(12);
        mScatterPlot.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.CENTER);
        for( Double valorY: puntos  ){
            series.appendData(new DataPoint( i , valorY   ),true,1000);
            i++;
        }
        series.setColor(Color.RED);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(5);
        mScatterPlot.addSeries(series);
    }

    private  void graficaDos( ArrayList<Double> puntosVectorR, long tiempo){

        // Transformar de ArryaList a Double, porque la funcion fft slo acpeta double[]
        double[] vector = new double[puntosVectorR.size()];
        int i = 0;
        for( Double valorY: puntosVectorR  ){
            vector[i] = valorY;
            i++;
        }
        //Log.d("VectorR FOURIER",""+vector.length);
        //Log.d("Tiempo FOURIER",""+tiempo);
        //Log.d("frameSegundo FOURIER",""+frmSegundo);

        // Funcion FFT.
        DoubleFFT_1D fft = new DoubleFFT_1D(vector.length);
        fft.realForward( vector );
        // LIsta de valores complejos
        List<Complex> listaComplejo = new ArrayList<Complex>();
        for(i = 0; i < vector.length / 2; ++i) {
            double re  = vector[2*i];
            double im  = vector[2*i+1];
            Complex complejo = new Complex(re, im);
            listaComplejo.add(complejo);
        }

        //Magnitud
        double[] magnitudP1 = new double[ listaComplejo.size() ];
        i = 0;
        //System.out.println("Tamaño del vector magnitud " + listaComplejo.size() );
        for (Complex complex : listaComplejo) {
            magnitudP1[i] = (complex.abs()/vector.length) * 2;
            //System.out.println(complex.toString() + "Mag" + complex.abs() );
            i++;
        }

        /*
        * Fs = 913 defecto
        * T = 1/Fs
        * L = total de frames.
        * Y = ftt(R)    FFT del vector R
        * P1 = abs(Y/L)
        * */

        //int Fs = 913;
        double Fs = (913*tiempo)/60;
        int L = vector.length;
        double[] frecuencia = new double[vector.length/2];
        for(int q = 0;q< listaComplejo.size() ;q++  ) {
            frecuencia[q] = (Fs*q)/L;
        }

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();

        mScatterPlot2.getViewport().setScalableY(true);
        mScatterPlot2.setTitle("Varaicón de la Frecuencia");
        mScatterPlot2.getGridLabelRenderer().setTextSize(12);
        mScatterPlot2.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.RIGHT);

        Log.d("Tamaño de la Frecuencia",""+frecuencia.length);

        double mayorFrecuenciaRanog = 0.0;
        double frecuenciaEstimada = 0;
        for(int j = 1; j < frecuencia.length ; j++){
            if( frecuencia[j]>=55 && frecuencia[j]<=145){
                if (magnitudP1[j] > mayorFrecuenciaRanog) {
                    mayorFrecuenciaRanog = magnitudP1[j];
                    frecuenciaEstimada = frecuencia[j];
                    Log.d("FRecuencia Estimaada",""+frecuencia[j]);
                }
            }
            series2.appendData(new DataPoint( frecuencia[j] ,  magnitudP1[j]  ),true,1000);
        }

        series2.setDrawDataPoints(true);
        series2.setDataPointsRadius(5);
        mScatterPlot2.addSeries(series2);

        Log.d("Frecuencia final" ,""+ frecuenciaEstimada);
        DecimalFormat formato = new DecimalFormat("#.00");
        txtRitmoCardiacoP.setText( formato.format(frecuenciaEstimada) + "BPM");


    }
}
