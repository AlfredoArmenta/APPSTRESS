package com.example.estres2.ui.registros

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.R
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.archivo.Archivo
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.databinding.FragmentRegistroBinding
import com.example.estres2.ui.viewmodel.MenuViewModel
import java.io.File

class FragmentRegistro: Fragment() {
    private var _binding: FragmentRegistroBinding? = null
    private val binding get() = _binding!!
    private val menuViewModel: MenuViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var user: Usuario
    private val lRegistro: MutableList<ListaRegistro> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = binding.root.context
        setObservers()
        showRegister()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setObservers() {
        menuViewModel.apply {
            updateRegisters.observe(viewLifecycleOwner) {
                when (it) {
                    true -> {
                        Toast.makeText(context, "Se actualizo el estado del observador: $it", Toast.LENGTH_LONG).show()
                        showRegister()
                    }
                }
            }
        }
    }

    private fun showRegister() {
        val arrayFiles = obtenerRegistros()
        lRegistro.clear()
        if (arrayFiles.isNotEmpty()) {
            for (element in arrayFiles) {
                lRegistro.add(ListaRegistro(element, R.drawable.ic_registros_menu))
            }
        } else {
            Toast.makeText(mContext, "Carpeta Vacia", Toast.LENGTH_LONG).show()
            lRegistro.add(ListaRegistro("Carpeta Vacia", R.drawable.ic_sin_registro))
        }
        val itemDecoration = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(mContext, R.drawable.divider)?.let {
            itemDecoration.setDrawable(it)
        }
        binding.Resgistros.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)
            adapter = RegisterListAdapter(lRegistro, menuViewModel)
            addItemDecoration(itemDecoration)
        }
    }

    fun obtenerRegistros(): List<String> {
        val item: MutableList<String> = ArrayList()
        val db = DB(mContext)
        user = getObjectBoleta()
        val folder = File(Environment.getExternalStorageDirectory().path + "/Monitoreo" + user.boleta)
        Toast.makeText(mContext, folder.path, Toast.LENGTH_LONG).show()
        if (folder.exists() && !folder.listFiles().isNullOrEmpty()) {
            folder.listFiles().also {
                if (!it.isNullOrEmpty()) {
                    for (file in (it)) if (file.path.endsWith(".csv") && db.getRecord(Archivo(file.name, user.boleta)))
                        item.add(file.name)
                }
            }
            return item
        } else {
            Toast.makeText(mContext, "No existe la carpeta del usuario", Toast.LENGTH_LONG).show()
        }
        return item
    }
}