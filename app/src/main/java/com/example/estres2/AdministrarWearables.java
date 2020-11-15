package com.example.estres2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.almacenamiento.entidades.wearable.Wearable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdministrarWearables extends AppCompatActivity {
    // Creamos los objetos
    private RecyclerView rvWearable;
    private List<Wearable> wearablesLista = new ArrayList<>();
    public Spinner spinnerWearable;
    private ArrayList<Wearable> listaWearables = new ArrayList<>(Arrays.asList(new Wearable("1", "34:15:13:22:A1:5B"),
            new Wearable("2", "78:DB:2F:BF:34:2E"), new Wearable("3", "78:DB:2F:BF:43:7C"), new Wearable("4", "34:15:13:22:AE:90"),
            new Wearable("5", "F8:30:02:09:14:F9")));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_wearable);
        IniciarObjetos();
    }

    private void IniciarObjetos() {
        // Inicializamos los objetos
        spinnerWearable = (Spinner) findViewById(R.id.Spinner_Wearable);
        // String que nos ayudan a llenar a los spinners que sirven para la selección del semestre y la materia
        String[] semestre = {"Selecciona un dispositivo", listaWearables.get(0).getId(), listaWearables.get(1).getId(),
                listaWearables.get(2).getId(), listaWearables.get(3).getId(), listaWearables.get(4).getId()};
        ArrayAdapter<String> AdapterSemestre = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, semestre);
        spinnerWearable.setAdapter(AdapterSemestre);
        rvWearable = findViewById(R.id.rvWearable);
        rvWearable.setLayoutManager(new GridLayoutManager(this, 1));
        obtenerWearables();
    }

    // Función en la que obtenemos los parametros2
    private void obtenerWearables() {
        DB bd = new DB(getApplicationContext());
        wearablesLista.clear();
        wearablesLista = bd.MostrarWearable();
        ListaWearable lista = new ListaWearable(AdministrarWearables.this, wearablesLista);
        rvWearable.setAdapter(lista);
    }

    public void registrarWearable(View view){
        DB bd = new DB(getApplicationContext());
        if (spinnerWearable.getSelectedItemPosition() > 0) {
            if (bd.InsertarWearable(listaWearables.get(spinnerWearable.getSelectedItemPosition() - 1)) > 0) {
                Toast.makeText(this, "Se inserto correctamente el Wearable", Toast.LENGTH_LONG).show();
                obtenerWearables();
            } else {
                Toast.makeText(this, "No se inserto ni madres", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(this, "No se ha seleccionado un Wearable", Toast.LENGTH_LONG).show();
        }
    }

    public void eliminarWearable(View view){
        DB bd = new DB(getApplicationContext());
        if (spinnerWearable.getSelectedItemPosition() > 0) {
            if (bd.BorrarWearable(listaWearables.get(spinnerWearable.getSelectedItemPosition() - 1)) > 0) {
                Toast.makeText(this, "Se Elimino correctamente el Wearable", Toast.LENGTH_LONG).show();
                obtenerWearables();
            } else {
                Toast.makeText(this, "No se Borro ni madres", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(this, "No se ha seleccionado un Wearable", Toast.LENGTH_LONG).show();
        }
    }
}
