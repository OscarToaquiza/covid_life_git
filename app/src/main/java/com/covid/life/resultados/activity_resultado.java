package com.covid.life.resultados;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.R;
import com.covid.life.login.activity_registration;

import org.w3c.dom.Text;

public class activity_resultado extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        int totalPuntaje = getIntent().getExtras().getInt("totalPuntaje");
        ImageView icono = findViewById(R.id.icono);
        TextView txtResultado = findViewById(R.id.tvResultado);
        TextView txtDetalle = findViewById(R.id.tvDetalle);
        TextView txtInformacion = findViewById(R.id.tvInformacion);
        TextView txtEnlace = findViewById(R.id.tvEnlace);
        Button btn =  findViewById(R.id.btn);

        if(totalPuntaje<3) {
            icono.setImageResource(R.drawable.ic_ino_sospechoso);
            txtResultado.setText("NO ES SOSPECHOSO");
            txtDetalle.setText("Según la información proporcionada, al momento usted no es un caso sospechoso " +
                    "de infección de Coronavirus. Le sugerimos que se quede en en casa y lea el instructivo que le " +
                    "enviaremos a continuación.");
            txtInformacion.setText("¡QUÉDATE EN CASA!\n\nINFORMACIÓN");
            txtEnlace.setVisibility(View.VISIBLE);
            txtEnlace.setText("https://www.salud.gob.ec/medidas-de-proteccion-basicas-contra-el-nuevo-coronavirus/");
            btn.setText("Salir");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else if (totalPuntaje>2 && totalPuntaje<5){
            icono.setImageResource(R.drawable.ic_baja_sospecha);
            txtResultado.setText("Baja SOSPECHA");
            txtDetalle.setText("Según la información proporcionada, al momento usted es un caso sospechoso de portar" +
                    " coronavirus, por lo que debe permanecer en su casa en aislamiento. A continuación le enviaremos el instructivo.");
            txtInformacion.setText("¡QUÉDATE EN CASA!\n\nLea atentamente estas recomendaciones y llame al 171. " +
                    "las personas con las que usted convive también deben recibir esta información.");
            txtEnlace.setVisibility(View.VISIBLE);
            txtEnlace.setText("https://www.salud.gob.ec/medidas-de-proteccion-basicas-contra-el-nuevo-coronavirus/");
            btn.setText("Salir");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        } else if (totalPuntaje>4 && totalPuntaje<8){
            icono.setImageResource(R.drawable.ic_alta_sospecha);
            txtResultado.setText("ALTA SOSPECHA");
            txtDetalle.setText("Según la información proporcionada, usted tiene una alta probabilidad de estar contagiado del coronavirus." +
                    " Quédese en casa y comuníquese inmediatamente con el 171.");
            txtInformacion.setText("¡QUÉDATE EN CASA!");
            txtEnlace.setVisibility(View.INVISIBLE);
            btn.setText("REGISTRATE");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), activity_registration.class);
                    startActivity(intent);
                }
            });

        } else if (totalPuntaje>7){
            icono.setImageResource(R.drawable.ic_atencion_urgente);
            txtResultado.setText("ATENCION URGENTE");
            txtDetalle.setText("LLAME AL 171 o 911");
            txtInformacion.setText("¡REQUIERE EVALUACIÓN MÉDICA INMEDIATA!");
            txtEnlace.setVisibility(View.INVISIBLE);
            btn.setText("REGISTRATE");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), activity_registration.class);
                    startActivity(intent);
                }
            });

        }


    }


}
