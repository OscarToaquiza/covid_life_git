package com.covid.life.form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.covid.life.R;

public class activity_recuerda extends AppCompatActivity {
    private Button btncontinuarTemp;
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuerda);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btncontinuarTemp = findViewById(R.id.btnContinuarTemp);

        btncontinuarTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(),
                        activity_temperatura.class);
                startActivity(intent);
            }
        });
    }
}
