package com.example.estres2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.archivo.Archivo
import com.example.estres2.databinding.ActivityMostrararchivosBinding

class MostrarArchivos : AppCompatActivity() {

    private lateinit var binding: ActivityMostrararchivosBinding
    private lateinit var db: DB
    private lateinit var ArchivoLista: List<Archivo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMostrararchivosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        db = DB(applicationContext)
        ArchivoLista = db.showRecord()
        binding.rvArchivo.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = AdapterArchivos(context, ArchivoLista)
        }
    }
}