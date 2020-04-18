package com.covid.life.notificaciones;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.covid.life.R;

public class activity_llamada extends AppCompatActivity {
    private TextView txtEnlacellamada;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String enlace = getIntent().getExtras().getString("enlace");
        txtEnlacellamada = findViewById(R.id.tvEnlaceLlamada);
        txtEnlacellamada.setText(enlace);

    }
}
