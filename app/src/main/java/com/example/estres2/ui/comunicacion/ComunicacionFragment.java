package com.example.estres2.ui.comunicacion;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.Usuario;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ComunicacionFragment extends Fragment {

    public Usuario user;

    private Spinner UA;

    public ComunicacionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        PedirPermisos();

        ExportarCSV();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_comunicacion, container, false);

        UA = (Spinner) root.findViewById(R.id.CMMateria);

        String [] UnidadAprendizaje = {"Selecciona una materia", "Lineas de Transmisión y Antenas", "Teoría de la Información", "Teoría de las Comunicaciones", "Variable Compleja",
                "Protocolos de Internet", "Comunicaciones Digitales", "Sistemas Distribuidos", "Metodología", "Sistemas Celulares", "Multimedia","Señales y Sistemas", "Probabilidad",
                "Programación de Dispositivos Móviles", "PT1", "PT2"};
        ArrayAdapter<String> AdapterUnidad = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, UnidadAprendizaje);
        UA.setAdapter(AdapterUnidad);

        return root;
    }

    public void PedirPermisos() {

        if ( ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    public void ExportarCSV() {
        user = ((MenuPrincipal)getActivity()).MandarUsuario();
        DateFormat hourdateFormat = new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy");
        File Carpeta = new File(Environment.getExternalStorageDirectory() + "/Monitoreo" + user.getBoleta());

        boolean isCreate = false;

        if (!Carpeta.exists()) {
            isCreate = Carpeta.mkdir();
        }

        String[] arregloArchivos = Carpeta.list();
        assert arregloArchivos != null;
        int numArchivos = arregloArchivos.length; // NÚMERO DE ARCHIVOS EN LA CARPETA

        Toast.makeText(getContext(), hourdateFormat.format(new Date()), Toast.LENGTH_SHORT).show();

        String Archivo = Carpeta.toString() + "/" + user.getBoleta() + "_" + hourdateFormat.format(new Date()).trim() + ".csv" ;

        try {
            FileWriter fileWriter = new FileWriter(Archivo);
            fileWriter.append(user.getBoleta()).append("\n");
            fileWriter.append(hourdateFormat.format(new Date())).append("\n");
            fileWriter.append("MuestraFC, TiempoFC, MuestraGSR, TiempoGSR");
            fileWriter.close();
            Toast.makeText(getContext(), "Se creo correctmente el registro de las variables.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
