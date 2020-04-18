package com.covid.life.form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.covid.life.R;

public class activity_guia extends AppCompatActivity {
    Button btnSiguiente;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        btnSiguiente = findViewById(R.id.btnContinuar);

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),
                        activity_recuerda.class);
                startActivity(intent);
            }
        });
    }
}
