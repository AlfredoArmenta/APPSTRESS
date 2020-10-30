package com.example.estres2.ui.cerrar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.estres2.InicioSesion;
import com.example.estres2.R;

public class CerrarSesionFragment extends Fragment {

    public CerrarSesionFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);
        Button CSboton = root.findViewById(R.id.CSButton);

        CSboton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Salir();
            }
        });

        return root;
    }

    public void Salir() {
        Intent siguiente = new Intent(getContext(), InicioSesion.class);
        startActivity(siguiente);
        getActivity().finish();
    }
}
