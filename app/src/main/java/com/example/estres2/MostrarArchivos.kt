package com.example.estres2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.archivo.RegisterFile
import com.example.estres2.databinding.ActivityShowFilesBinding

class MostrarArchivos : AppCompatActivity() {

    private lateinit var binding: ActivityShowFilesBinding
    private lateinit var db: DB
    private lateinit var registerFileLista: List<RegisterFile>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowFilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        db = DB(applicationContext)
        registerFileLista = db.showRecord()
        binding.rvArchivo.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = AdapterArchivos(context, registerFileLista)
        }
    }
}