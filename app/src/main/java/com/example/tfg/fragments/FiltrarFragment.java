package com.example.tfg.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tfg.BuscarActivity;
import com.example.tfg.R;


public class FiltrarFragment extends Fragment {

   View view;
   CardView conocerGente_cv, aventura_cv, fiesta_cv, chill_cv;




    public FiltrarFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filtrar, container, false);
        conocerGente_cv = view.findViewById(R.id.conocerGente_cv);
        aventura_cv = view.findViewById(R.id.aventura_cv);
        fiesta_cv = view.findViewById(R.id.fiesta_cv);
        chill_cv = view.findViewById(R.id.chill_cv);



        


        conocerGente_cv.setOnClickListener(new View.OnClickListener() { //Se ejecutara el metodo showBuscarActivity() en el que te pide un String category, segun la imagen seleccionada el String category cambia
            @Override
            public void onClick(View v) {
                showBuscarActivity("Conocer Gente");
            }
        });

        aventura_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuscarActivity("Aventura");
            }
        });

        fiesta_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuscarActivity("Fiesta");
            }
        });

        chill_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBuscarActivity("Chill");
            }
        });

        return view;
    }



    public void showBuscarActivity(String category){ //Te lleva a la activity BuscarActivity junto con el contenido del String category
        Intent intent = new Intent(getContext(), BuscarActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }
}