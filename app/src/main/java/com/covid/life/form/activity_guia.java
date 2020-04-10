package com.covid.life.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.covid.life.R;

public class activity_guia extends AppCompatActivity {
    Button btnSiguiente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);
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
