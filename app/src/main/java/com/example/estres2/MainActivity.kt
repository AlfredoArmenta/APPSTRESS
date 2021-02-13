package com.example.estres2

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import com.example.estres2.actividades.bluetooth.ConectarBluno
import com.example.estres2.actividades.iniciosesion.InicioSesion
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.util.reduceBitmap
import com.example.estres2.databinding.ActivityMenuPrincipalBinding
import com.example.estres2.util.requestPermissionExternalStorage

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuPrincipalBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var hView: View
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var userName: TextView
    private lateinit var userBoleta: TextView
    private lateinit var userData: Usuario
    private lateinit var userImage: ImageView
    private lateinit var addImageView: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        initializeObjects()
    }

    private fun setObservers() {
        mainViewModel.apply {
            updateUserData.observe(owner = this@MainActivity) {
                println("Entro al observer")
                if (it) {
                    userData = getObjectBoleta()
                    userName.text = userData.nombre
                    userBoleta.text = userData.boleta
                    println("ESe actualizó el Usuario")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_Cerrar_Sesión -> {
                println("Se puchurro la opción de cerrar sesión")
                backLogging()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val db = DB(applicationContext)
        if (resultCode == RESULT_OK) {
            userData.imagen = data?.dataString!!
            if (db.updateImage(userData)) {
                Toast.makeText(applicationContext, "Se guardo correctamente la URL de la imagen", Toast.LENGTH_LONG).show()
                userImage.setImageBitmap(reduceBitmap(applicationContext, data.dataString, 512f, 512f))
            } else {
                Toast.makeText(applicationContext, "Ocurrio un error al guardar la URL de la imagen", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeObjects() {
        binding.apply {
            appBarMenu.apply {
                setSupportActionBar(toolbar)
                fab.setOnClickListener {
                    goToBluno()
                }
            }
            mAppBarConfiguration = AppBarConfiguration.Builder(
                    R.id.nav_inicio, R.id.nav_graficas, R.id.nav_registro,
                    R.id.nav_configurar_cuenta, R.id.nav_eliminar)
                    .setOpenableLayout(drawerLayout)
                    .build()
            navController = Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment)
            NavigationUI.setupActionBarWithNavController(this@MainActivity, navController, mAppBarConfiguration)
            NavigationUI.setupWithNavController(navView, navController)
            hView = navView.getHeaderView(0)
            userImage = hView.findViewById(R.id.MenuImagen)
            addImageView = hView.findViewById(R.id.add_image)
            addImageView.setOnClickListener {
                openGallery()
            }
        }
        setupUserInformation()
        requestPermissionExternalStorage(applicationContext, this)
    }

    private fun setupUserInformation() {
        userData = getObjectBoleta()
        userName = hView.findViewById(R.id.MenuNombre)
        userName.text = (userData.nombre)
        userBoleta = hView.findViewById(R.id.MenuBoleta)
        userBoleta.text = (userData.boleta)
        when {
            userData.imagen == "" -> {
                Toast.makeText(applicationContext, "No sea seleccionado una imagen", Toast.LENGTH_LONG).show()
            }
            reduceBitmap(applicationContext, userData.imagen, 512f, 512f) != null -> {
                Toast.makeText(applicationContext, "Se cargo la imagen correctamente", Toast.LENGTH_LONG).show()
                userImage.setImageBitmap(reduceBitmap(applicationContext, userData.imagen, 512f, 512f))
            }
            else -> {
                Toast.makeText(applicationContext, "La imagen no se encuentra", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun goToBluno() {
        startActivity(Intent(this@MainActivity, ConectarBluno::class.java))
        finish()
    }

    private fun openGallery() {
        println("Se entró a la función openGallery")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), 10)
    }

    private fun backLogging() {
        startActivity(Intent(this@MainActivity, InicioSesion::class.java))
        finish()
    }
}