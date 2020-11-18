package com.example.estres2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.almacenamiento.entidades.archivo.Archivo;

import java.util.ArrayList;
import java.util.List;

public class MostrarArchivos extends AppCompatActivity {
    // Creamos los objetos
    RecyclerView rvArchivo;
    AdapterArchivos Lista;
    List<Archivo> ArchivoLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializamos los objetos
        setContentView(R.layout.activity_mostrararchivos);
        rvArchivo = findViewById(R.id.rvArchivo);
        rvArchivo.setLayoutManager(new GridLayoutManager(this, 1));
        obtenerArchivos();
    }

    // Función en la que obtenemos los parametros2
    public void obtenerArchivos() {
        ArchivoLista.clear();
        DB db = new DB(getApplicationContext());
        ArchivoLista = db.MostrarArchivo();
        Lista = new AdapterArchivos(MostrarArchivos.this, ArchivoLista);
        rvArchivo.setAdapter(Lista);
    }
    // ********************** Fin de la clase Mostrar ************************ //
}