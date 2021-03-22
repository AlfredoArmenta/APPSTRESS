package com.example.estres2.ui.inico

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.estres2.R
import com.example.estres2.almacenamiento.entidades.cardview.CardView
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.FragmentHomeBinding
import java.util.Calendar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var dataUser: User

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
        setCardView()
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

    private fun setCardView() {
        val cards = listOf(
                CardView(R.drawable.ic_add_image,
                        "Título 1",
                        "Subtítulo 1"),
                CardView(R.drawable.ic_add_image,
                        "Título 2",
                        "Subtítulo 2"),
                CardView(R.drawable.ic_add_image,
                        "Título 3",
                        "Subtítulo 3")
        )
        binding.apply {
            viewPagerCards.apply {
                adapter = CardViewAdapter(cards)
            }
            indicators.apply {
                setViewPager(viewPagerCards)
                setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
            }
        }
    }
}