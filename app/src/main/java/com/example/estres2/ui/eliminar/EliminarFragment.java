package com.example.estres2.ui.eliminar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.estres2.UsuarioBoleta;
import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.actividades.iniciosesion.InicioSesion;
import com.example.estres2.R;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;

import java.io.File;

public class EliminarFragment extends Fragment {
    private TextView txtEliminar;
    private Button Eliminar;
    private Usuario user;
    private DB bd;

    public EliminarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eliminar, container, false);
        txtEliminar = view.findViewById(R.id.txt_eliminar);
        Eliminar = view.findViewById(R.id.CEliminar);
        Eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarUsuario();
            }
        });
        user = UsuarioBoleta.INSTANCE.getObjectBoleta();
        bd = new DB(getContext());
        return view;
    }

    private void EliminarUsuario() {
        File Carpeta = new File(Environment.getExternalStorageDirectory() + "/Monitoreo" + user.getBoleta());
        if (Carpeta.exists()) {
            deleteRecursive(Carpeta);
            if (!Carpeta.exists()) {
                System.out.println("Carpeta borrada correctamente");
                if (bd.EliminarArchivos(user.getBoleta()) > 0) {
                    System.out.println("Archivos borrados de la base de datos correctamente");
                    BorrarUsuario();
                    Regresar();
                } else {
                    System.out.println("Los archivos no fueron borrados de la base de datos correctamente");
                }
            } else {
                System.out.println("La carpeta no fue borrada correctamente");
            }
        } else {
            System.out.println("La carpeta no existe");
            BorrarUsuario();
            Regresar();
        }
    }

    private void BorrarUsuario() {
        if (bd.BorrarUsuario(user.getBoleta()) > 0) {
            System.out.println("Se elimino el usuario con boleta: " + user.getBoleta() + " y con nombre: " + user.getNombre());
        } else {
            System.out.println("No se pudo eliminar el usuario con boleta: " +
                    user.getBoleta() + " y con nombre: " + user.getNombre());
        }
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }

    private void Regresar() {
        Intent siguiente = new Intent(getContext(), InicioSesion.class);
        startActivity(siguiente);
        getActivity().finish();
    }
}