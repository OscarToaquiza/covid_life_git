package com.covid.life.manage.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
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
    private Button btnSendData;
    private ImageButton btnStartTime;
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

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PERNISSION_CAMERA);
            }
        }
        //--
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_camera_data);
        // Inicializar vista de camara
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.camaraOpenCV);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        // Inicializar objetos de la vista
        // Objetos
        mChronometer = (Chronometer) findViewById(R.id.ChronometerTime);
        btnStartTime = findViewById(R.id.btnIniciarTiempo);
        btnSendData = findViewById(R.id.btnEnviarTiempo);
        // Variables
        mDataPoints = new ArrayList<Double>();
        channels = new ArrayList<Mat>();
        // desabilitsr boton de grabacion
        btnSendData.setEnabled(mIsRecord);



        // Activar cronometro e inicio de obtencion de datos para dibujar la grafica.
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsRecord){
                    mIsRecord = true;
                    btnSendData.setEnabled(!mIsRecord);
                    btnStartTime.setImageResource(R.drawable.icon_play_red);
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mChronometer.start();
                    showElapsedTime();
                }else{
                    mIsRecord = false;
                    btnSendData.setEnabled(!mIsRecord);
                    btnStartTime.setImageResource(R.drawable.icon_play_green);
                    //Toast.makeText(getApplicationContext(), "Tiempo transurrido" + mChronometer.getText().toString(),Toast.LENGTH_SHORT).show();

                    mChronometer.stop();
                    showElapsedTime();


                }
            }
        });

        // Volver y analizar.
        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onBackPressed();
                Intent formularioSintomas = new Intent(v.getContext() , pre_camera.class);
                formularioSintomas.putExtra("Puntos", mDataPoints );
                formularioSintomas.putExtra("Segundos", secondTotal );
                startActivity(formularioSintomas);
                finish();
            }
        });
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

            PX1 =   Core.mean( channelRed.submat(            10,           12,           10,           12 )).val[0];
            PX2 =   Core.mean( channelRed.submat( ( witdhFRame/2 )-2,( witdhFRame/2 )+2,          10,           12 )).val[0];
            PX3 =   Core.mean( channelRed.submat(     witdhFRame -12,    witdhFRame -10,          10,           12 )).val[0];

            PX4 =   Core.mean( channelRed.submat(            10,           12,(heigthFrame/2) - 2,( heigthFrame/2 )+ 2) ).val[0];
            PX5 =   Core.mean( channelRed.submat(( witdhFRame/2 )- 2,(witdhFRame/2) + 2,(heigthFrame/2) - 2,( heigthFrame/2 )+ 2) ).val[0];
            PX6 =   Core.mean( channelRed.submat(    witdhFRame - 12,    witdhFRame -10,(heigthFrame/2) - 2,( heigthFrame/2 )+ 2) ).val[0];

            PX7 =   Core.mean( channelRed.submat(            10,           12,   heigthFrame - 12,    heigthFrame - 10 )).val[0];
            PX8 =   Core.mean( channelRed.submat(( witdhFRame/2 )- 2,(witdhFRame/2) + 2,    heigthFrame -12,    heigthFrame - 10 )).val[0];
            PX9 =   Core.mean( channelRed.submat(    witdhFRame - 12,    witdhFRame -10,    heigthFrame -12,    heigthFrame - 10) ).val[0];

            valueY = (PX1+PX2+PX3+PX4+PX5+PX6+PX7+PX8 +PX9) / 9;
            // Agregando los puntos de la gráfica
            mDataPoints.add(valueY);

            Log.d("TAMANIOARRAY",""+ mDataPoints.size()) ;

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
}
