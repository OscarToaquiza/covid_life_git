package com.covid.life.form;

import android.net.Uri;
import android.os.Bundle;

import com.covid.life.R;
import com.covid.life.fragments.fragment_instrucciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.covid.life.form.ui.main.SectionsPagerAdapter;

public class activity_test extends AppCompatActivity   {
    private LinearLayout linearPuntos;
    private  ViewPager viewPager;
    private TextView[] puntosSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        linearPuntos = findViewById(R.id.idLinearPuntos);
        agregarIndicadorPuntos(0);
        viewPager.addOnPageChangeListener(viewListener);



    }

    private void agregarIndicadorPuntos(int pos) {
        puntosSlide = new TextView[4];
        linearPuntos.removeAllViews();

        for(int i=0 ; i< puntosSlide.length; i++){
            puntosSlide[i] = new TextView(this);
            puntosSlide[i].setText(Html.fromHtml("&#8226"));
            puntosSlide[i].setTextSize(35);
            puntosSlide[i].setTextColor(getResources().getColor(R.color.colorBlancoTransparente));
            linearPuntos.addView(puntosSlide[i]);
        }

        if(puntosSlide.length > 0){
            puntosSlide[pos].setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }



    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            cambiarPagina(position);

        }

        @Override
        public void onPageSelected(int position) {
            agregarIndicadorPuntos(position);

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void cambiarPagina(final int position){
        Button btnInstrucciones =  findViewById(R.id.btnInstrucciones);
        Button btnInicio =  findViewById(R.id.btnInicio);
        Button btnFiebreSi =  findViewById(R.id.btnFiebreSi);
        Button btnFiebreNo =  findViewById(R.id.btnFiebreNo);
        Button btnTosSi =  findViewById(R.id.btnTosSi);
        Button btnTosNo =  findViewById(R.id.btnTosSi);
        switch (position) {
            case 0 :
                btnInstrucciones.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(1);
                    }
                });
            break;

            case 1 :
                btnInicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(2);
                    }
                });
                break;

            case 2:
                btnFiebreSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(3);
                    }
                });
                btnFiebreNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(3);
                    }
                });


        }

    }

}