package com.covid.life.form.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.covid.life.R;
import com.covid.life.fragments.fragment_actividades;
import com.covid.life.fragments.fragment_comidas;
import com.covid.life.fragments.fragment_contagio;
import com.covid.life.fragments.fragment_diarrea;
import com.covid.life.fragments.fragment_fiebre;
import com.covid.life.fragments.fragment_inicio;
import com.covid.life.fragments.fragment_instrucciones;
import com.covid.life.fragments.fragment_respirar;
import com.covid.life.fragments.fragment_tos;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    public static Fragment newInstance(int index) {
        Fragment fragment = null;
        switch (index){
            case 1: fragment = new fragment_instrucciones(); break;
            case 2: fragment = new fragment_inicio(); break;
            case 3: fragment = new fragment_fiebre(); break;
            case 4: fragment = new fragment_tos(); break;
            case 5: fragment = new fragment_comidas(); break;
            case 6: fragment = new fragment_respirar(); break;
            case 7: fragment = new fragment_actividades(); break;
            case 8: fragment = new fragment_diarrea(); break;
            case 9: fragment = new fragment_contagio(); break;

        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_activity_test, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}