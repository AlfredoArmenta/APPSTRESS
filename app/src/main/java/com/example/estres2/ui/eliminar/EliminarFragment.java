package com.example.estres2.ui.eliminar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.example.estres2.Usuario;

public class EliminarFragment extends Fragment {
    private TextView txtEliminar;
    private Button Eliminar;

    public EliminarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (!(getActivity() == null)) {
            Usuario user = ((MenuPrincipal) getActivity()).MandarUsuario();
            DB bd = new DB(getContext());
            if (bd.BorrarUsuario(user.getBoleta()) > 0) {
                Toast.makeText(getContext(), "Se elimino el usuario con boleta: " + user.getBoleta()
                        + " y con nombre: " + user.getNombre(), Toast.LENGTH_SHORT).show();
                Intent siguiente = new Intent(getContext(), InicioSesion.class);
                startActivity(siguiente);
                (getActivity()).finish();
            } else {
                Toast.makeText(getContext(), "No se pudo eliminar el usuario con boleta: " +
                        user.getBoleta() + " y con nombre: " + user.getNombre(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("Eliminar", "El getActivity() de EliminarUsuario esta vacio");
        }
    }
}