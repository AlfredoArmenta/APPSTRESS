package com.example.estres2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Mostrar extends AppCompatActivity {

    // Creamos los objetos
    RecyclerView rvUsuarios;
    ListaUsuarios Lista;
    List<Usuario> UsuariosLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos los objetos
        setContentView(R.layout.activity_mostrar);
        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new GridLayoutManager(this, 1));
        obtenerUsuarios();

    }

    // Función en la que obtenemos los parametros2
    public void obtenerUsuarios() {
        UsuariosLista.clear();

        DB db = new DB(getApplicationContext());

        Cursor fila = db.Mostrar();

        if(fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            do {
                UsuariosLista.add(
                        new Usuario(
                                fila.getString(0),
                                fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4),
                                fila.getString(5),
                                fila.getString(6)
                        )
                );
            } while(fila.moveToNext());
        } else {
            Toast.makeText(Mostrar.this, "No hay registros.", Toast.LENGTH_LONG).show();
        }

        Lista = new ListaUsuarios(Mostrar.this, UsuariosLista);
        rvUsuarios.setAdapter(Lista);

    }

    // ********************** Fin de la clase Mostrar ************************ //
}
