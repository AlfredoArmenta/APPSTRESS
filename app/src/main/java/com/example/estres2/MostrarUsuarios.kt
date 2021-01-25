package com.example.estres2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.databinding.ActivityMostrarusuarioBinding

class MostrarUsuarios : AppCompatActivity() {

    private lateinit var binding: ActivityMostrarusuarioBinding
    private lateinit var db: DB
    private lateinit var UsuariosLista: List<Usuario>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrarusuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        db = DB(applicationContext)
        UsuariosLista = db.showUser()
        binding.rvUsuarios.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = AdapterUsuarios(context, UsuariosLista)
        }
    }
}