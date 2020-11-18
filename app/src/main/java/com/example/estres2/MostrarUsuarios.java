package com.example.estres2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class MostrarUsuarios extends AppCompatActivity {
    // Creamos los objetos
    RecyclerView rvUsuarios;
    AdapterUsuarios Lista;
    List<Usuario> UsuariosLista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inicializamos los objetos
        setContentView(R.layout.activity_mostrarusuario);
        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new GridLayoutManager(this, 1));
        obtenerUsuarios();
    }

    // Función en la que obtenemos los parametros2
    public void obtenerUsuarios() {
        UsuariosLista.clear();
        DB db = new DB(getApplicationContext());
        UsuariosLista = db.MostrarUsuario();
        Lista = new AdapterUsuarios(MostrarUsuarios.this, UsuariosLista);
        rvUsuarios.setAdapter(Lista);
    }
    // ********************** Fin de la clase Mostrar ************************ //
}

