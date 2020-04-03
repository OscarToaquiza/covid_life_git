package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jtransforms.fft.DoubleFFT_1D;
import org.opencv.android.OpenCVLoader;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class pre_camera extends AppCompatActivity {

    Button btnIrTemperatura;

    private  ArrayList<Double> puntos;
    private GraphView mScatterPlot,mScatterPlot2;
    private long seconds;

    private  TextView txtRitmoCardiacoP;

    private TextView segundos,frmes,framesSegundos;

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
            graficaUno();
            Log.d("TamanioVecotr",""+puntos.size());
            Log.d("Tiempo",""+seconds);
            Log.d("Tiempo",""+(puntos.size()/seconds));

            graficaDos(puntos,seconds);

        }

    }


    private  void graficaUno(){
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

    private  void graficaDos( ArrayList<Double> puntosVectorR, long tiempo){

        double[] vector = new double[puntosVectorR.size()];
        int i = 0;
        for( Double valorY: puntosVectorR  ){
            vector[i] = valorY;
            i++;
        }

        long frmSegundo = (puntosVectorR.size()/tiempo);
        Log.d("VectorR FOURIER",""+vector.length);
        Log.d("Tiempo FOURIER",""+tiempo);
        Log.d("frameSegundo FOURIER",""+frmSegundo);

        DoubleFFT_1D fft = new DoubleFFT_1D( vector.length );
        //fft.complexForward(vector);
        fft.realForward(vector);
        /*
        * Fs = 100 100 muestras por cada 10 segundos
        * T = 1/Fs
        * L = 200 Tiempo.
        * Y = ftt(R)    FFT del vector R
        * P2 = abs(Y/L)
        * */

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        Double ejex = 0.0;
        mScatterPlot2.getViewport().setScalable(true);
        mScatterPlot2.getViewport().setMinY(0);
        mScatterPlot2.getViewport().setMaxY(300);
        mScatterPlot2.getViewport().setXAxisBoundsManual(true);

        double f = 0.0;
        double g = 0.0;
        for(int j = 1; j < vector.length; j++){
         /*   if ( vector[j]> f){
                f = vector[j];
                Log.d("ECIADOR",""+ f);
            }*/
            series.appendData(new DataPoint( ejex ,  (Math.abs(vector[j]))  ),true,1000);
            ejex++;
        }


        mScatterPlot2.addSeries(series);

        i = 0;

        for (int x = 0; x < vector.length; x++) {
            for ( i = 0; i < vector.length-x-1; i++) {
                if(vector[i] < vector[i+1]){
                    double tmp = vector[i+1];
                    vector[i+1] = vector[i];
                    vector[i] = tmp;
                }
            }
        }

        i = 0;
        double promedioGeneral = 0.0;
        double[] vectorPromedio = new double[ ( (int) frmSegundo/2) ];
        for( i = 0; i < vectorPromedio.length; i++){
            vectorPromedio[i] = vector[i+1];
            promedioGeneral = promedioGeneral + vector[i+1];
        }

        Log.d("ArrayProm",Arrays.toString(vectorPromedio));
        promedioGeneral = promedioGeneral/( (int) frmSegundo/2);
        Log.d("ArrayProm", "" + promedioGeneral );

        DecimalFormat formato = new DecimalFormat("#.00");

        txtRitmoCardiacoP.setText(""+ formato.format(promedioGeneral) + " BMP");
        //txtRitmoCardiacoP.setText(""+ (vector[0]+vector[1]+vector[3])/3 + " BMP");


    }
}
