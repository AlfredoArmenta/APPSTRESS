package com.example.estres2.ui.cuenta

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.estres2.MainActivity
import com.example.estres2.MainViewModel
import com.example.estres2.R
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.util.UserObject.setObjectBoleta
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.FragmentAccountBinding
import com.example.estres2.util.setIconDrawableAndChangeColor

class AccountFragment : Fragment() {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private var correctNombre: Boolean = true
    private var correctEdad: Boolean = true
    private var correctPassword: Boolean = true
    private lateinit var user: User
    private lateinit var bd: DB
    private lateinit var mContext: Context
    private lateinit var notification: NotificationCompat.Builder

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = binding.root.context
        initializeObjects()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeObjects() {
        val semester = arrayOf("Selecciona tu semestre actual", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
        val adapterSemester = ArrayAdapter(mContext, R.layout.spinner_custom, semester)
        user = getObjectBoleta()
        bd = DB(mContext)
        binding.apply {
            CFBoleta.text = user.boleta
            CFNombre.apply {
                editText?.setText(user.nombre)
                startIconDrawable = resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_sort_alphabetically, R.color.correct_green)
                editText?.doOnTextChanged { text, _, _, _ ->
                    correctNombre = when {
                        text.isNullOrEmpty() -> {
                            editText?.setError(getString(R.string.SinNombre), null)
                            false
                        }
                        else -> {
                            editText?.setError(null, null)
                            true
                        }
                    }
                    startIconDrawable = if (correctNombre) {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_sort_alphabetically, R.color.correct_green)
                    } else {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_sort_alphabetically, R.color.error_red)
                    }
                }
            }
            CFEdad.apply {
                editText?.setText(user.edad)
                startIconDrawable = resources.setIconDrawableAndChangeColor(R.drawable.ic_age, R.color.correct_green)
                editText?.doOnTextChanged { text, _, _, _ ->
                    correctEdad = when {
                        text.isNullOrEmpty() -> {
                            editText?.setError(getString(R.string.SinEdad), null)
                            false
                        }
                        text.toString().toInt() !in 20..25 -> {
                            editText?.setError(getString(R.string.RangoEdad), null)
                            false
                        }
                        else -> {
                            editText?.setError(null, null)
                            true
                        }
                    }
                    startIconDrawable = if (correctEdad) {
                        resources.setIconDrawableAndChangeColor(R.drawable.ic_age, R.color.correct_green)
                    } else {
                        resources.setIconDrawableAndChangeColor(R.drawable.ic_age, R.color.error_red)
                    }
                }
            }
            if (user.genero == "Masculino") {
                CFMasculino.isChecked = true
            } else {
                CFFemenino.isChecked = true
            }
            CFSemestre.apply {
                adapter = adapterSemester
                setSelection(adapterSemester.getPosition(user.semestre))
            }
            CFPassword.apply {
                editText?.setText(user.password)
                CFLongitud.setTextColor(Color.GREEN)
                CFCaracterEspecial.setTextColor(Color.GREEN)
                CFNumero.setTextColor(Color.GREEN)
                CFMinuscula.setTextColor(Color.GREEN)
                CFMayuscula.setTextColor(Color.GREEN)
                startIconDrawable = resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.correct_green)
                editText?.doOnTextChanged { text, _, _, _ ->
                    if (text.isNullOrEmpty()) {
                        editText?.setError(getString(R.string.SinContraseña), null)
                    } else {

                        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
                        if (text.length !in 8..15) {
                            CFLongitud.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_too_short_password), null)
                        } else {
                            CFLongitud.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún caracter especial
                        if (!text.contains(".*[!@#$%^*+=¿?_-].*".toRegex())) {
                            CFCaracterEspecial.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_special_caracter), null)
                        } else {
                            CFCaracterEspecial.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún digito
                        if (!text.contains(".*\\d.*".toRegex())) {
                            CFNumero.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_number), null)
                        } else {
                            CFNumero.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna minúscula
                        if (!text.contains(".*[a-z].*".toRegex())) {
                            CFMinuscula.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_lowercase_caracter), null)
                        } else {
                            CFMinuscula.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna mayúscula
                        if (!text.matches(".*[A-Z].*".toRegex())) {
                            CFMayuscula.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_uppercase_caracter), null)
                        } else {
                            CFMayuscula.setTextColor(Color.GREEN)
                        }
                        correctPassword = CFLongitud.currentTextColor == Color.GREEN &&
                                CFCaracterEspecial.currentTextColor == Color.GREEN &&
                                CFNumero.currentTextColor == Color.GREEN &&
                                CFMinuscula.currentTextColor == Color.GREEN &&
                                CFMayuscula.currentTextColor == Color.GREEN
                        startIconDrawable = if (correctPassword) {
                            resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.correct_green)
                        } else {
                            resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.error_red)
                        }
                    }
                }
            }
            CFAplicar.setOnClickListener {
                updateData()
            }
        }
    }

    private fun updateData() {
        val bd = DB(mContext)
        binding.apply {
            user.boleta = CFBoleta.text.toString()
            user.nombre = CFNombre.editText?.text.toString()
            user.edad = CFEdad.editText?.text.toString()
            if (CFMasculino.isChecked) {
                user.genero = "Masculino"
            } else {
                user.genero = "Femenino"
            }
            user.semestre = CFSemestre.selectedItem.toString()
            user.password = CFPassword.editText?.text.toString()
            user.imagen = user.imagen
            if (correctNombre && correctEdad && binding.CFSemestre.selectedItem.toString() != "Selecciona tu semestre actual" && correctPassword) {
                if (bd.updateUser(user)) {
                    setObjectBoleta(user)
                    mainViewModel.updateUserDataFunc(true)
                    Toast.makeText(context, "Se actualizo correctamente.", Toast.LENGTH_SHORT).show()
                    setNotification()
                } else {
                    Toast.makeText(context, "Ocurrio un error al actualizar.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setNotification() {
        notification = NotificationCompat.Builder(requireContext(), MainActivity.CHANNEL_0_ID).apply {
            setContentTitle("Cuenta")
            setContentText("Se ha actualizado tu cuenta")
            setSubText("Información Personal")
            setSmallIcon(R.drawable.ic_login)
            color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            priority = NotificationCompat.PRIORITY_LOW
        }
        NotificationManagerCompat.from(requireContext()).apply {
            notify(MainActivity.NOTIFICATION_0, notification.build())
        }
    }
}
