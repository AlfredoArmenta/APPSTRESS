package com.example.estres2.ui.inico;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InicioFragment extends Fragment {

    private TextView inicio;

    public InicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_inicio, container, false);
        Saludos(root);

        return root;
    }

    public void Saludos (View view){
        Usuario user = ((MenuPrincipal)getActivity()).MandarUsuario();

        DateFormat hourFormat = new SimpleDateFormat("HH");

        inicio = (TextView) view.findViewById(R.id.txt_inicio);

        String Hora = hourFormat.format(new Date());

        int EstadoDia = Integer.parseInt(Hora);

        if (EstadoDia>=6 && EstadoDia<12){
            inicio.setText("Hola " + user.getNombre() + " Buenos dias");

        }else if(EstadoDia>=12 && EstadoDia<18){
            inicio.setText("Hola " + user.getNombre() + " Buenas tardes");

        }else if(EstadoDia>=18 && EstadoDia<=23){
            inicio.setText("Hola " + user.getNombre() + " Buenas noches");

        }else{
            inicio.setText("Hola " + user.getNombre() + " ¡Ya es tarde deberías dormir, para comenzar un buen día!");

        }

    }
}
