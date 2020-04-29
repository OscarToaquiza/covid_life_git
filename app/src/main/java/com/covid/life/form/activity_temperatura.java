package com.covid.life.form;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.covid.life.R;
import com.covid.life.menu.activity_menu_inicio;
import com.covid.life.menu.menu_pacientes;

public class activity_temperatura extends AppCompatActivity {
    private Button btnAgregarSignosVitales;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnAgregarSignosVitales = findViewById(R.id.btnAgregarSignos);

        btnAgregarSignosVitales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
