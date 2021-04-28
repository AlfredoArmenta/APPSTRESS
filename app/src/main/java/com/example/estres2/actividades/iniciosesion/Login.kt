package com.example.estres2.actividades.iniciosesion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.estres2.*
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.util.UserObject.isInitialized
import com.example.estres2.util.UserObject.setObjectBoleta
import com.example.estres2.actividades.recuperarpassword.RecoverPassword
import com.example.estres2.actividades.registrar.Register
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.ActivityLoginBinding
import com.example.estres2.util.setIconDrawableAndChangeColor

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var correctBoleta: Boolean = false
    private var correctPassword: Boolean = false
    private lateinit var bd: DB
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeObjects()
        UsuariosFake()
    }

    private fun initializeObjects() {
        binding.apply {
            User.apply {
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
                            editText?.setError(null, null)
                            true
                        }
                    }
                    startIconDrawable = if (correctBoleta) {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_edit, R.color.correct_green)
                    } else {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_edit, R.color.error_red)
                    }
                }
            }
            Password.apply {
                startIconDrawable = resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.error_red)
                editText?.doOnTextChanged { text, _, _, _ ->
                    correctPassword = when {
                        text.isNullOrEmpty() -> {
                            editText?.setError(getString(R.string.SinContraseña), null)
                            false
                        }
                        text.length !in 8..15 -> {
                            editText?.setError(getString(R.string.Longitud_de_8_15_Caracteres), null)
                            false
                        }
                        else -> {
                            editText?.setError(null, null)
                            true
                        }
                    }
                    startIconDrawable = if (correctPassword) {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.correct_green)
                    } else {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.error_red)
                    }
                }
            }
            Loggin.setOnClickListener {
                if (correctBoleta && correctPassword) {
                    bd = DB(applicationContext)
                    bd.getUser(User.editText?.text.toString())?.let { userIt -> setObjectBoleta(userIt) }
                    if (isInitialized()) {
                        when (Password.editText?.text.toString()) {
                            getObjectBoleta().password -> {
                                Toast.makeText(applicationContext, getText(R.string.InicioSesion), Toast.LENGTH_SHORT).show()
                                setNextActivity(Intent(this@Login, MainActivity::class.java))
                            }
                            else -> {
                                Toast.makeText(applicationContext, getText(R.string.ErrorContraseña), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, getText(R.string.BoletaNoRegistrada), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, if (!correctBoleta) {
                        getString(R.string.ErrorBoleta)
                    } else if (!correctPassword) {
                        getString(R.string.ErrorContraseña)
                    } else {
                        ""
                    }, Toast.LENGTH_LONG).show()
                }
            }
            ForgotPassword.setOnClickListener {
                setNextActivity(Intent(this@Login, RecoverPassword::class.java))
                finish()
            }
            buttonRegisterInicioSesion.setOnClickListener {
                setNextActivity(Intent(this@Login, Register::class.java))
                finish()
            }

            MostrarArchivo.setOnClickListener {
                setNextActivity(Intent(this@Login, MostrarArchivos::class.java))
            }

            MostrarUsuario.setOnClickListener {
                setNextActivity(Intent(this@Login, MostrarUsuarios::class.java))
            }
        }
    }

    private fun setNextActivity(nextActivity: Intent) {
        startActivity(nextActivity)
    }

    private fun UsuariosFake() {
        val bd = DB(applicationContext)
        user = User("2015640017",
                "Alfredo Armenta Espinosa",
                "24",
                "Masculino",
                "12",
                "Politecnico12@",
                "")
        bd.insertUser(user)
        user = User("2015640000",
                "Fulanito Fulano Fulanote",
                "24",
                "Masculino",
                "12",
                "Politecnico12@",
                "")
        bd.insertUser(user)
        user = User("2015640408",
                "Efraín Villegas Sánchez",
                "24",
                "Masculino",
                "12",
                "Tru\$tn01",
                "")
        bd.insertUser(user)
    }
}
