package com.example.estres2.ui.inico

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.estres2.R
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import com.example.estres2.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        Saludos(root)
        return root
    }

    fun Saludos(view: View) {
        val (_, nombre) = getObjectBoleta()
        @SuppressLint("SimpleDateFormat") val hourFormat: DateFormat = SimpleDateFormat("HH")
        val inicio = view.findViewById<View>(R.id.txt_inicio) as TextView
        val Hora = hourFormat.format(Date())
        val EstadoDia = Hora.toInt()
        if (EstadoDia >= 6 && EstadoDia < 12) {
            inicio.text = String.format("Hola %s Buenos dias", nombre)
        } else if (EstadoDia >= 12 && EstadoDia < 18) {
            inicio.text = String.format("Hola %s Buenas tardes", nombre)
        } else if (EstadoDia >= 18 && EstadoDia <= 23) {
            inicio.text = String.format("Hola %s Buenas noches", nombre)
        } else {
            inicio.text = String.format("Hola %s ¡Ya es tarde deberías dormir, para comenzar un buen día!", nombre)
        }
    }
}