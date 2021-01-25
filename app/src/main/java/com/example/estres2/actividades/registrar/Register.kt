package com.example.estres2.actividades.registrar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.core.widget.doOnTextChanged
import com.example.estres2.R
import com.example.estres2.actividades.iniciosesion.InicioSesion
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.databinding.ActivityRegisterBinding

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var correctBoleta: Boolean = false
    private var correctNombre: Boolean = false
    private var correctEdad: Boolean = false
    private var correctSemestre: Boolean = false
    private var correctPassword: Boolean = false
    private lateinit var user: Usuario
    private lateinit var bd: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        IniciarObjetos()
    }

    private fun IniciarObjetos() {
        val semester = arrayOf("Selecciona tu semestre actual", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15")
        val adapterSemester = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, semester)
        binding.apply {
            RMasculino.isChecked = true

            RBoleta.apply {
                doOnTextChanged { text, _, _, _ ->
                    correctBoleta = when {
                        text.isNullOrEmpty() -> {
                            containerBoleta.startIconDrawable = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_edit)
                            setError(getString(R.string.SinBoleta), null)
                            false
                        }
                        text.length != 10 -> {
                            setError(getString(R.string.LongBoleta), null)
                            false
                        }
                        else -> {
                            setError(null, null)
                            true
                        }
                    }
                }
            }
            RNombre.apply {
                doOnTextChanged { text, _, _, _ ->
                    correctNombre = when {
                        text.isNullOrEmpty() -> {
                            setError(getString(R.string.SinNombre), null)
                            false
                        }
                        else -> true
                    }
                }
            }
            REdad.apply {
                doOnTextChanged { text, _, _, _ ->
                    correctEdad = when {
                        text.isNullOrEmpty() -> {
                            setError(getString(R.string.SinEdad), null)
                            false
                        }
                        text.toString().toInt() !in 20..25 -> {
                            setError(getString(R.string.RangoEdad), null)
                            false
                        }
                        else -> true
                    }
                }
            }
            RSemestre.apply {
                adapter = adapterSemester
                setBackgroundColor(getColor(R.color.colorPrimary))
            }
            RPassword.apply {
                doOnTextChanged { text, _, _, _ ->
                    if (text.isNullOrEmpty()) {
                        this.setError(getString(R.string.SinContraseña), null)
                    } else {

                        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
                        if (text.length !in 8..15) {
                            RTLongitud.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_too_short_password), null)
                        } else {
                            RTLongitud.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún caracter especial
                        if (!text.contains(".*[!@#$%^*+=¿?_-].*".toRegex())) {
                            RTCaracterEspecial.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_special_caracter), null)
                        } else {
                            RTCaracterEspecial.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún digito
                        if (!text.contains(".*\\d.*".toRegex())) {
                            RTNumero.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_number), null)
                        } else {
                            RTNumero.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna minúscula
                        if (!text.contains(".*[a-z].*".toRegex())) {
                            RTMinuscula.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_lowercase_caracter), null)
                        } else {
                            RTMinuscula.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna mayúscula
                        if (!text.matches(".*[A-Z].*".toRegex())) {
                            RTMayuscula.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_uppercase_caracter), null)
                        } else {
                            RTMayuscula.setTextColor(Color.GREEN)
                        }
                        correctPassword = RTLongitud.currentTextColor == Color.GREEN &&
                                RTCaracterEspecial.currentTextColor == Color.GREEN &&
                                RTNumero.currentTextColor == Color.GREEN &&
                                RTMinuscula.currentTextColor == Color.GREEN &&
                                RTMayuscula.currentTextColor == Color.GREEN
                    }
                }
            }
            RRegistrar.setOnClickListener {
                user = Usuario("", "", "", "", "", "", "")
                bd = DB(applicationContext)
                user.boleta = RBoleta.text.toString()
                user.nombre = RNombre.text.toString()
                user.edad = REdad.text.toString()
                if (RMasculino.isChecked) {
                    user.genero = "Masculino"
                } else {
                    user.genero = "Femenino"
                }
                user.semestre = RSemestre.selectedItem.toString()
                user.password = RPassword.text.toString()
                user.imagen = ""
                if (correctBoleta && correctNombre && correctEdad && binding.RSemestre.selectedItem.toString() != "Selecciona tu semestre actual" && correctPassword) {
                    if (bd.insertUser(user)) {
                        Toast.makeText(applicationContext, getText(R.string.InicioCorrecto), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, InicioSesion::class.java))
                        finish()
                    } else {
                        Toast.makeText(applicationContext, getText(R.string.BoletaRegistrada), Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(applicationContext, "Algún campo es incorrecto revisalos", Toast.LENGTH_SHORT).show()
                }
            }
            RCancel.setOnClickListener {
                startActivity(Intent(applicationContext, InicioSesion::class.java))
                finish()
            }
        }
    }
}