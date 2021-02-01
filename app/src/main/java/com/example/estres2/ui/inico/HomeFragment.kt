package com.example.estres2.ui.inico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.databinding.FragmentHomeBinding
import java.util.Calendar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataUser: Usuario

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userToRegards()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun userToRegards() {
        dataUser = getObjectBoleta()
        val hourFormat = Calendar.getInstance()
        val dayHour = hourFormat.get(Calendar.HOUR_OF_DAY)
        binding.apply {
            txtInicio.apply {
                text = when (dayHour) {
                    in 6..11 -> {
                        String.format("¡Hola ${dataUser.nombre} Buenos días!")
                    }
                    in 12..17 -> {
                        String.format("¡Hola ${dataUser.nombre} Buenas tardes!")
                    }
                    in 18..23 -> {
                        String.format("¡Hola ${dataUser.nombre} Buenas noches!")
                    }
                    else -> {
                        String.format("¡Hola ${dataUser.nombre} Ya es tarde deberías dormir, para comenzar un buen día!")
                    }
                }
            }
        }
    }
}