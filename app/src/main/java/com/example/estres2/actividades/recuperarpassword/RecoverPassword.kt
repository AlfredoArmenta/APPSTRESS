package com.example.estres2.actividades.recuperarpassword

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.estres2.R
import com.example.estres2.actividades.iniciosesion.InicioSesion
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.databinding.ActivityRecoverPasswordBinding

class RecoverPassword : AppCompatActivity() {
    private lateinit var binding: ActivityRecoverPasswordBinding
    private var correctBoleta: Boolean = false
    private var correctPassword: Boolean = false
    private lateinit var bd: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoverPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        InitializeObjetos()
    }

    private fun InitializeObjetos() {
        bd = DB(applicationContext)
        binding.apply {
            CBoleta.apply {
                doOnTextChanged { text, _, _, _ ->
                    correctBoleta = when {
                        text.isNullOrEmpty() -> {
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

            CPassword.apply {
                doOnTextChanged { text, _, _, _ ->
                    if (text.isNullOrEmpty()) {
                        this.setError(getString(R.string.SinContraseña), null)
                    } else {

                        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
                        if (text.length !in 8..15) {
                            CTLongitud.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_too_short_password), null)
                        } else {
                            Toast.makeText(context, "Ya tiene la longitud", Toast.LENGTH_LONG).show()
                            CTLongitud.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún caracter especial
                        if (!text.contains(".*[!@#$%^*+=¿?_-].*".toRegex())) {
                            CTCaracterEspecial.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_special_caracter), null)
                        } else {
                            CTCaracterEspecial.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene algún digito
                        if (!text.contains(".*\\d.*".toRegex())) {
                            CTNumero.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_number), null)
                        } else {
                            CTNumero.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna minúscula
                        if (!text.contains(".*[a-z].*".toRegex())) {
                            CTMinuscula.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_lowercase_caracter), null)
                        } else {
                            CTMinuscula.setTextColor(Color.GREEN)
                        }

                        // Preguntamos si se contiene alguna mayúscula
                        if (!text.matches(".*[A-Z].*".toRegex())) {
                            CTMayuscula.setTextColor(Color.RED)
                            this.setError(getString(R.string.error_not_find_uppercase_caracter), null)
                        } else {
                            CTMayuscula.setTextColor(Color.GREEN)
                        }
                        correctPassword = CTLongitud.currentTextColor == Color.GREEN &&
                                CTCaracterEspecial.currentTextColor == Color.GREEN &&
                                CTNumero.currentTextColor == Color.GREEN &&
                                CTMinuscula.currentTextColor == Color.GREEN &&
                                CTMayuscula.currentTextColor == Color.GREEN
                    }
                }
            }
            CAceptar.setOnClickListener {
                if (correctBoleta && correctPassword) {
                    if (bd.recoverPassword(CBoleta.text.toString(), CPassword.text.toString())) {
                        Toast.makeText(applicationContext, getText(R.string.ActualizoCorrectamente), Toast.LENGTH_SHORT).show()
                        startActivity(Intent(applicationContext, InicioSesion::class.java))
                        finish()
                    } else {
                        CBoleta.error = getString(R.string.BoletaNoEncontrada)
                        Toast.makeText(applicationContext, getText(R.string.BoletaNoRegistrada), Toast.LENGTH_SHORT).show()
                    }
                } else if (CBoleta.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, R.string.SinBoleta, Toast.LENGTH_LONG).show()
                } else if (CPassword.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, R.string.SinContraseña, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}