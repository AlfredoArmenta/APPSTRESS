package com.example.estres2.ui.eliminar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estres2.DB;
import com.example.estres2.InicioSesion;
import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.RecuperarPassword;
import com.example.estres2.Usuario;

public class EliminarFragment extends Fragment {

    private TextView txtEliminar;
    private Button Eliminar;

    public EliminarFragment() {
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

        View root = inflater.inflate(R.layout.fragment_eliminar, container, false);

        txtEliminar = (TextView) root.findViewById(R.id.txt_eliminar);
        Eliminar = (Button) root.findViewById(R.id.CEliminar);

        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarUsuario();
            }
        });


        return root;
    }

    public void EliminarUsuario() {
        Usuario user = ((MenuPrincipal)getActivity()).MandarUsuario();
        DB bd = new DB(getContext());
        bd.BorrarUsuario(user.getBoleta());
        Intent siguiente = new Intent(getContext(), InicioSesion.class);
        startActivity(siguiente);
        Toast.makeText(getContext(), "El usuario con boleta: " + user.getBoleta() + " y con nombre: " + user.getNombre(), Toast.LENGTH_SHORT).show();
        (getActivity()).finish();
    }

}
