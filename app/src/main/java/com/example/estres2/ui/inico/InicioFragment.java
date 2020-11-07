package com.example.estres2.ui.inico;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InicioFragment extends Fragment {
    public InicioFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        Saludos(root);
        return root;
    }

    public void Saludos(View view) {
        if (!(getActivity() == null)) {
            Usuario user = ((MenuPrincipal) getActivity()).MandarUsuario();
            @SuppressLint("SimpleDateFormat") DateFormat hourFormat = new SimpleDateFormat("HH");
            TextView inicio = (TextView) view.findViewById(R.id.txt_inicio);
            String Hora = hourFormat.format(new Date());
            int EstadoDia = Integer.parseInt(Hora);

            if (EstadoDia >= 6 && EstadoDia < 12) {
                inicio.setText(String.format("Hola %s Buenos dias", user.getNombre()));
            } else if (EstadoDia >= 12 && EstadoDia < 18) {
                inicio.setText(String.format("Hola %s Buenas tardes", user.getNombre()));
            } else if (EstadoDia >= 18 && EstadoDia <= 23) {
                inicio.setText(String.format("Hola %s Buenas noches", user.getNombre()));
            } else {
                inicio.setText(String.format("Hola %s ¡Ya es tarde deberías dormir, para comenzar un buen día!", user.getNombre()));
            }
        } else {
            Log.d("getActivity", "La actividad es nulla");
        }
    }
}
