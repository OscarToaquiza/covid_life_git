package com.covid.life.form;

import android.content.Intent;
import android.os.Bundle;

import com.covid.life.R;
import com.covid.life.resultados.activity_resultado;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.covid.life.form.ui.main.SectionsPagerAdapter;

public class activity_test extends AppCompatActivity   {
    private LinearLayout linearPuntos;
    private  ViewPager viewPager;
    private TextView[] puntosSlide;
    private int[] puntaje ;
    private int totalPuntaje;

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

        puntaje = new int[9];

    }

    private void agregarIndicadorPuntos(int pos) {
        puntosSlide = new TextView[9];
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
        Button btnTosNo =  findViewById(R.id.btnTosNo);
        Button btnComdasSi =  findViewById(R.id.btnComidasSi);
        Button btnComidasNo =  findViewById(R.id.btnComidasNo);
        Button btnRespirarSi =  findViewById(R.id.btnRespirarSi);
        Button btnRespirarNo =  findViewById(R.id.btnRespirarNo);
        Button btnActividadesSi =  findViewById(R.id.btnActividadesSi);
        Button btnActividadesNo =  findViewById(R.id.btnActividadesNo);
        Button btnDiarreaSi =  findViewById(R.id.btnDiarreaSi);
        Button btnDiarreaNo =  findViewById(R.id.btnDiarreaNo);
        Button btnContagioSi =  findViewById(R.id.btnContagioSi);
        Button btnContagioNo =  findViewById(R.id.btnContagioNo);



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
                        puntaje[0]=2;
                    }
                });
                btnFiebreNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(3);
                        puntaje[0]=0;
                    }
                });
                break;

            case 3:
                btnTosSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(4);
                        puntaje[1]=2;
                    }
                });
                btnTosNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(4);
                        puntaje[1]=0;
                    }
                });
                break;

            case 4:
                btnComdasSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(5);
                        puntaje[2]=1;
                    }
                });
                btnComidasNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(5);
                        puntaje[2]=0;
                    }
                });
                break;

            case 5:
                btnRespirarSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(6);
                        puntaje[3]=6;
                    }
                });
                btnRespirarNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(6);
                        puntaje[3]=0;
                    }
                });
                break;

            case 6:
                btnActividadesSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(7);
                        puntaje[4]=1;
                    }
                });
                btnActividadesNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(7);
                        puntaje[4]=0;
                    }
                });
                break;

            case 7:
                btnDiarreaSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(8);
                        puntaje[5]=1;
                    }
                });
                btnDiarreaNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(8);
                        puntaje[5]=0;
                    }
                });
                break;

            case 8:
                btnContagioSi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(9);
                        puntaje[6]=4;
                        totalPuntaje = calcularPuntaje(puntaje);
                        finish();
                        Intent intent = new Intent(getApplicationContext(), activity_resultado.class);
                        intent.putExtra("totalPuntaje", totalPuntaje);
                        startActivity(intent);
                    }
                });
                btnContagioNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(9);
                        puntaje[6]=0;
                        totalPuntaje = calcularPuntaje(puntaje);
                        finish();
                        Intent intent = new Intent(getApplicationContext(), activity_resultado.class);
                        intent.putExtra("totalPuntaje", totalPuntaje);
                        startActivity(intent);
                    }
                });
                break;



        }

    }

    private int calcularPuntaje(int [] puntaje){
        int total=0;
        for(int i=0 ; i< puntaje.length; i++){
            total = puntaje[i]+total;
        }
        return total;
    }

}