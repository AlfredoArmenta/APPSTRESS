package com.example.estres2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.ActivityShowUsersBinding

class MostrarUsuarios : AppCompatActivity() {
    private lateinit var binding: ActivityShowUsersBinding
    private lateinit var db: DB
    private lateinit var usuariosLista: List<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        db = DB(applicationContext)
        usuariosLista = db.showUser()
        binding.rvUsuarios.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = AdapterUsuarios(context, usuariosLista)
        }
    }
}