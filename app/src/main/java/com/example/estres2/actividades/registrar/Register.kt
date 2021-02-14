package com.example.estres2.actividades.registrar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.estres2.R
import com.example.estres2.actividades.iniciosesion.Login
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.ActivityRegisterBinding
import com.example.estres2.util.setIconDrawableAndChangeColor

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var correctBoleta: Boolean = false
    private var correctNombre: Boolean = false
    private var correctEdad: Boolean = false
    private var correctPassword: Boolean = false
    private lateinit var user: User
    private lateinit var bd: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeObjects()
    }

    private fun initializeObjects() {
        bd = DB(applicationContext)
        val semester = arrayOf("Selecciona tu semestre actual", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
        val adapterSemester = ArrayAdapter(applicationContext, R.layout.spinner_custom, semester)
        binding.apply {
            RMasculino.isChecked = true
            RBoleta.apply {
                startIconDrawable = resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_edit, R.color.error_red)
                editText?.doOnTextChanged { text, _, _, _ ->
                    correctBoleta = when {
                        text.isNullOrEmpty() -> {
                            editText?.setError(getString(R.string.SinBoleta), null)
                            false
                        }
                        text.length != 10 -> {
                            editText?.setError(getString(R.string.LongBoleta), null)
                            false
                        }
                        else -> {
                            if (bd.checkUser(editText?.text.toString())) {
                                Toast.makeText(context, getString(R.string.BoletaRegistrada), Toast.LENGTH_LONG).show()
                                false
                            } else {
                                editText?.setError(null, null)
                                true
                            }
                        }
                    }
                    startIconDrawable = if (correctBoleta) {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_edit, R.color.correct_green)
                    } else {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_edit, R.color.error_red)
                    }
                }
            }
            RNombre.apply {
                startIconDrawable = resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_sort_alphabetically, R.color.error_red)
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
            REdad.apply {
                startIconDrawable = resources.setIconDrawableAndChangeColor(R.drawable.ic_age, R.color.error_red)
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
            RSemestre.apply {
                adapter = adapterSemester
            }
            RPassword.apply {
                startIconDrawable = resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.error_red)
                editText?.doOnTextChanged { text, _, _, _ ->
                    if (text.isNullOrEmpty()) {
                        editText?.setError(getString(R.string.SinContraseña), null)
                    } else {

                        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
                        if (text.length !in 8..15) {
                            RTLongitud.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_too_short_password), null)
                        } else {
                            RTLongitud.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún caracter especial
                        if (!text.contains(".*[!@#$%^*+=¿?_-].*".toRegex())) {
                            RTCaracterEspecial.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_special_caracter), null)
                        } else {
                            RTCaracterEspecial.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún digito
                        if (!text.contains(".*\\d.*".toRegex())) {
                            RTNumero.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_number), null)
                        } else {
                            RTNumero.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna minúscula
                        if (!text.contains(".*[a-z].*".toRegex())) {
                            RTMinuscula.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_lowercase_caracter), null)
                        } else {
                            RTMinuscula.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna mayúscula
                        if (!text.matches(".*[A-Z].*".toRegex())) {
                            RTMayuscula.setTextColor(Color.RED)
                            editText?.setError(getString(R.string.error_not_find_uppercase_caracter), null)
                        } else {
                            RTMayuscula.setTextColor(Color.GREEN)
                        }
                        correctPassword = RTLongitud.currentTextColor == Color.GREEN &&
                                RTCaracterEspecial.currentTextColor == Color.GREEN &&
                                RTNumero.currentTextColor == Color.GREEN &&
                                RTMinuscula.currentTextColor == Color.GREEN &&
                                RTMayuscula.currentTextColor == Color.GREEN
                        startIconDrawable = if (correctPassword) {
                            resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.correct_green)
                        } else {
                            resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.error_red)
                        }
                    }
                }
            }
            RRegistrar.setOnClickListener {
                user = User("", "", "", "", "", "", "")
                bd = DB(applicationContext)
                user.boleta = RBoleta.editText?.text.toString()
                user.nombre = RNombre.editText?.text.toString()
                user.edad = REdad.editText?.text.toString()
                if (RMasculino.isChecked) {
                    user.genero = "Masculino"
                } else {
                    user.genero = "Femenino"
                }
                user.semestre = RSemestre.selectedItem.toString()
                user.password = RPassword.editText?.text.toString()
                user.imagen = ""
                if (correctBoleta && correctNombre && correctEdad && binding.RSemestre.selectedItem.toString() != "Selecciona tu semestre actual" && correctPassword) {
                    if (bd.insertUser(user)) {
                        Toast.makeText(applicationContext, getText(R.string.InicioCorrecto), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, Login::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, getString(R.string.error_cant_inser_user), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, getString(R.string.error_some_fiel_is_wrong), Toast.LENGTH_SHORT).show()
                }
            }
            RCancel.setOnClickListener {
                startActivity(Intent(applicationContext, Login::class.java))
                finish()
            }
        }
    }

    // Se anula el botón que nos regresa
    override fun onBackPressed() {}
}