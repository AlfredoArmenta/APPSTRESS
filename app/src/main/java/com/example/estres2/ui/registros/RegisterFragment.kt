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
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.MainViewModel
import com.example.estres2.R
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.FragmentRegistersBinding
import java.io.File
import com.example.estres2.util.SampEn
import kotlinx.coroutines.cancel

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegistersBinding? = null
    private val binding get() = _binding!!
    private val menuViewModel: MainViewModel by viewModels()
    private lateinit var mContext: Context
    private lateinit var user: User
    private val lRegister: MutableList<UserRegister> = ArrayList()
    private val test: DoubleArray = doubleArrayOf(0.0, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = binding.root.context
        setObservers()
        showRegister()
        readRegister()
    }

    private fun readRegister() {

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
                        println("Se actualizo el estado del observador: $it")
                        showRegister()
                    }
                }
            }
        }
    }

    private fun showRegister() {
        val arrayFiles = getRegisters()
        lRegister.clear()
        if (arrayFiles.isNotEmpty()) {
            for (element in arrayFiles) {
                lRegister.add(UserRegister(element, R.drawable.ic_options_register))
            }
        } else {
            Toast.makeText(mContext, "Carpeta Vacia", Toast.LENGTH_LONG).show()
            lRegister.add(UserRegister("Carpeta Vacia", R.drawable.ic_not_registered))
        }
        val itemDecoration = DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(mContext, R.drawable.divider)?.let {
            itemDecoration.setDrawable(it)
        }
        binding.Resgistros.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)
            adapter = RegisterListAdapter(lRegister, menuViewModel)
            addItemDecoration(itemDecoration)
        }

        binding.Stress.text = "${SampEn(test, 3, 0.2)}"
    }

    private fun getRegisters(): List<String> {
        val item: MutableList<String> = ArrayList()
        val db = DB(mContext)
        user = getObjectBoleta()
        val folder = File(Environment.getExternalStorageDirectory().path + "/Monitoreo" + user.boleta)
        println("Ubicación de los archivos: ${folder.path}")
        if (folder.exists() && !folder.listFiles().isNullOrEmpty()) {
            folder.listFiles().also {
                if (!it.isNullOrEmpty()) {
                    for (file in (it)) if (file.path.endsWith(".csv") && db.getRecord(file.name))
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