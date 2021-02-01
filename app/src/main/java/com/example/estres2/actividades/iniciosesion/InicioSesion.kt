package com.example.estres2.actividades.iniciosesion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.estres2.*
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import com.example.estres2.UsuarioBoleta.isInitialized
import com.example.estres2.UsuarioBoleta.setObjectBoleta
import com.example.estres2.actividades.recuperarpassword.RecoverPassword
import com.example.estres2.actividades.registrar.Register
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.databinding.ActivityInicioSesionBinding
import com.example.estres2.util.setIconDrawableAndChangeColor

class InicioSesion : AppCompatActivity() {
    private lateinit var binding: ActivityInicioSesionBinding
    private val mainViewModel: MainViewModel by viewModels()
    private var correctBoleta: Boolean = false
    private var correctPassword: Boolean = false
    private lateinit var bd: DB
    private lateinit var user: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioSesionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        initializeObjects()
        UsuariosFake()
    }

    private fun setObservers() {
        mainViewModel.apply {
            boleta.observe(this@InicioSesion) {
            }
        }
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
                    startIconDrawable = if(correctBoleta){
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_menu_edit, R.color.correct_green)
                    }else {
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
                    startIconDrawable = if (correctPassword){
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.correct_green)
                    }else {
                        resources.setIconDrawableAndChangeColor(android.R.drawable.ic_lock_idle_lock, R.color.error_red)
                    }
                }
            }
            ForgotPassword.setOnClickListener {
                setNextActivity(Intent(this@InicioSesion, RecoverPassword::class.java))
            }
            Loggin.setOnClickListener {
                if (correctBoleta && correctPassword) {
                    bd = DB(applicationContext)
                    bd.getUser(User.editText?.text.toString())?.let { userIt -> setObjectBoleta(userIt) }
                    if (isInitialized()) {
                        when (Password.editText?.text.toString()) {
                            getObjectBoleta().password -> {
                                setNextActivity(Intent(this@InicioSesion, MenuPrincipal::class.java))
                            }

                            else -> {
                                Toast.makeText(applicationContext, getText(R.string.ErrorContraseña), Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(applicationContext, getText(R.string.BoletaNoRegistrada), Toast.LENGTH_SHORT).show()
                    }
                }
            }
            buttonRegisterInicioSesion.setOnClickListener {
                setNextActivity(Intent(this@InicioSesion, Register::class.java))
            }

            MostrarArchivo.setOnClickListener {
                setNextActivity(Intent(this@InicioSesion, MostrarArchivos::class.java))
            }

            MostrarUsuario.setOnClickListener {
                setNextActivity(Intent(this@InicioSesion, MostrarUsuarios::class.java))
            }
        }
    }

    private fun setNextActivity(nextActivity: Intent) {
        startActivity(nextActivity)
    }

    private fun UsuariosFake() {
        val bd = DB(applicationContext)
        user = Usuario("2015640017",
                "Alfredo Armenta Espinosa",
                "24",
                "12",
                "Masculino",
                "Politecnico12@",
                "")
        bd.insertUser(user)
        user = Usuario("2015640000",
                "Fulanito Fulano Fulanote",
                "24",
                "12",
                "Masculino",
                "Politecnico12@",
                "")
        bd.insertUser(user)
        user = Usuario("2015640408",
                "Efraín Villegas Sánchez",
                "24",
                "12",
                "Masculino",
                "Tru\$tn01",
                "")
        bd.insertUser(user)
    }
}