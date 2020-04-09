package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import com.covid.life.R;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

public class camera_data extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    //Codigo para manejar los permisos de la camara
    private static final  int PERNISSION_CAMERA = 1001;
    // Variables para manejar la camara con opencv.
    private static final String TAG = "OpenCV:Activ:cameradata";
    private CameraBridgeViewBase mOpenCvCameraView;

    //Variables para manejar el Cronometro
    private ImageButton btnSTratTime;
    private boolean mIsRecord = false;
    private Chronometer mChronometer;
    private Button btnStartTime;
    private long secondTotal;

    //Variables para manejar los frames de video
    private List<Mat> channels;
    private Mat viewGeneral, channelRed;
    private int heigthFrame, witdhFRame;

    //Variables para manejar el vetor
    private Double  PX1,PX2,PX3,PX4,PX5,PX6,PX7,PX8,PX9;
    private ArrayList<Double> mDataPoints;
    private Double valueY = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Verificar Permisos de la cammara... docuemtacion oficial rápida
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                Log.d("PERMISOS","PERMISOS ACEPTADO");
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERNISSION_CAMERA);
            }
        }
        //--
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera_data);
        // Inicializar vista de camara
        mOpenCvCameraView = findViewById(R.id.camaraOpenCV);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        // Inicializar objetos de la vista
        // Objetos
        mChronometer = findViewById(R.id.ChronometerTime);
        btnStartTime = findViewById(R.id.btnIniciarTiempo);
        // Variables
        mDataPoints = new ArrayList<Double>();
        channels = new ArrayList<Mat>();


        // Activar cronometro e inicio de obtencion de datos para dibujar la grafica.
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsRecord){
                    mIsRecord = true;
                    btnStartTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_play_red,0,0,0);
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    showElapsedTime();
                }else{
                    mIsRecord = false;
                    btnStartTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_play_green,0,0,0);
                    //Toast.makeText(getApplicationContext(), "Tiempo transurrido" + mChronometer.getText().toString(),Toast.LENGTH_SHORT).show();
                    mChronometer.stop();
                    showElapsedTime();
                }
            }
        });

    }

    private void irAtras(){
        long elapsedMillis = (SystemClock.elapsedRealtime() - mChronometer.getBase() );
        secondTotal = elapsedMillis / 1000;
        Intent formularioSintomas = new Intent( this , pre_camera.class);
        formularioSintomas.putExtra("Puntos", mDataPoints );
        formularioSintomas.putExtra("Segundos", secondTotal );
        startActivity(formularioSintomas);
        finish();
    }

    private void showElapsedTime() {
        long elapsedMillis = (SystemClock.elapsedRealtime() - mChronometer.getBase() );
        secondTotal = elapsedMillis / 1000;
        Toast.makeText(this, "Tiempo en segundos: " + secondTotal,
                Toast.LENGTH_SHORT).show();
    }

    // Metodos autogenerados
    @Override
    protected void onPause() {
        super.onPause();
        // Opencv Camara
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // OPencv camara
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback( this ) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                {
                    Log.i(TAG,"Opencv cargado correctamente");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setMaxFrameSize(300,300);
                    mOpenCvCameraView.disableFpsMeter();
                } break;
                default:{
                    super.onManagerConnected(status);
                } break;
            }

        }
    };

    @Override
    public void onCameraViewStarted(int width, int height) {
        // Obtener datos de inicio de camara
    }

    @Override
    public void onCameraViewStopped() {
        //viewGeneral.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        viewGeneral = inputFrame.rgba();

        witdhFRame = viewGeneral.rows(); //ANCHO
        heigthFrame = viewGeneral.cols(); //ALTO

        Log.d("Alto",""+heigthFrame);
        Log.d("Ancho",""+witdhFRame);

        Core.split(viewGeneral, channels);
        channelRed = channels.get(0);
        //blue = channels.get(1);
        //green = channels.get(2);

        if( mIsRecord ){
            //Log.d( "R",red.submat(            708,           710,          10,           12 ).dump());
            //Log.d( "-",red.submat(( ancho/2 )- 2,(ancho/2) + 2,(alto/2) - 2,( alto/2 )+ 2).dump());


            PX1 = getMatrizCentral(                       0,                   0, witdhFRame/3, heigthFrame/3);
            PX2 = getMatrizCentral(      witdhFRame /3,                   0, (witdhFRame/3) *2, heigthFrame/3);
            PX3 = getMatrizCentral(  (witdhFRame/3) *2,                   0,                witdhFRame, heigthFrame/3);

            PX4 = getMatrizCentral(                      0 , heigthFrame /3,    witdhFRame /3, (heigthFrame/3) *2 );
            PX5 = getMatrizCentral(     witdhFRame /3 , heigthFrame /3, (witdhFRame/3) *2, (heigthFrame/3) *2 );
            PX6 = getMatrizCentral(  (witdhFRame/3) *2, heigthFrame /3, witdhFRame, (heigthFrame/3) *2 );

            PX7 = getMatrizCentral(                     0 , (heigthFrame/3) *2, witdhFRame /3 , heigthFrame);
            PX8 = getMatrizCentral(    witdhFRame /3 , (heigthFrame/3) *2, (witdhFRame/3) *2, heigthFrame);
            PX9 = getMatrizCentral( (witdhFRame/3) *2 , (heigthFrame/3) *2, witdhFRame,            heigthFrame );


            valueY = (PX1+PX2+PX3+PX4+PX5+PX6+PX7+PX8 +PX9) / 9;
            // Agregando los puntos de la gráfica
            mDataPoints.add(valueY);

            //Log.d("TAMANIOARRAY",""+ mDataPoints.size()) ;
            long elapsedMillis = (SystemClock.elapsedRealtime() - mChronometer.getBase() );
            secondTotal = elapsedMillis / 1000;
            if(secondTotal >= 60){
                //Log.d("Se acabo el tiempo",""+secondTotal);
                this.irAtras();
            }

        }

        return viewGeneral;
        /*

        Puedo obtener la amtriz original y salcar la diagonal con diad(), despues se puede hacer na matriz transpuesta con
        core transpose donde el primer paramtro es la traiz original el segundo es el reultado, no es necesario asiganr,
        ya con el nuevo valor se piene la diagonal y esta es la diagonals eundaria de la primera matriz.

        Mat pr = red.submat(( ancho/2 )- 2,(ancho/2) + 2,(alto/2) - 2,( alto/2 )+ 2);
        Mat pr2 = new Mat();
        Core.transpose(pr,pr2);
        Log.d( "Inversa", pr2.dump()   );

        //Log.d( "Funcion diag", pr.diag().dump() );



        //Log.d( "Funcion diag",  Core.mean( pr.diag() ).toString() );


         */
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    // devemos dividir a la imagen 6 cuadrantes.
    /*
    |1|2|3|
    |4|5|6|
    |7|8|9|

    Enviar los puntos iniciales, y los puntos finales del cuadrante. a ese punto le resto -2 +2

    Se debe enviar como parametros los siguientes:
    int posicionXInicial, int posicionXFinal, int posicionYInicial, int posicionYFinal
     */

    private Double getMatrizCentral(  int xi, int yi, int xf, int yf   ){
        Double promedioMatrizPIxeles = 0.0;

        int xo = xf;
        int yo = yi;

        int xm = ( (xf -xi)/2 ) + xi;
        int ym = ( (yf -yi)/2 ) + yo;

        promedioMatrizPIxeles =   Core.mean( channelRed.submat(xm - 2,xm + 2, ym - 2,ym + 2) ).val[0];

        return  promedioMatrizPIxeles;

    }
}
