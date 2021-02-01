package com.example.estres2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import com.example.estres2.actividades.bluetooth.ConectarBluno
import com.example.estres2.actividades.iniciosesion.InicioSesion
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.ui.viewmodel.MenuViewModel
import com.example.estres2.util.reduceBitmap
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MenuPrincipal : AppCompatActivity() {
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private val menuViewModel: MenuViewModel by viewModels()
    @JvmField
    var Nombre: TextView? = null
    protected var Boleta: TextView? = null
    protected var DatosUsuario: Usuario? = null
    private var FotoUsuario: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        Toast.makeText(applicationContext, getText(R.string.InicioSesion), Toast.LENGTH_SHORT).show()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { pasarBlunoConectar() }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_graficas, R.id.nav_registro,
                R.id.nav_configurar_cuenta, R.id.nav_eliminar)
                .setOpenableLayout(drawer)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration!!)
        NavigationUI.setupWithNavController(navigationView, navController)
        // Se crea un componente View para poder mostrar el contenido
        val Hview = navigationView.getHeaderView(0)
        FotoUsuario = Hview.findViewById(R.id.MenuImagen)
        pasarBoleta(Hview)
        // Permisos para almacenamiento externo
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_principal, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(navController, mAppBarConfiguration!!)
                || super.onSupportNavigateUp())
    }

    fun pasarBoleta(view: View) {
        // Se realiza la consulta y se guarda en un onjeto usuario
        DatosUsuario = getObjectBoleta()
        Nombre = view.findViewById(R.id.MenuNombre)
        Nombre?.text = (DatosUsuario?.nombre)
        Boleta = view.findViewById(R.id.MenuBoleta)
        Boleta?.text = (DatosUsuario?.boleta)
        if (DatosUsuario!!.imagen == "") {
            Toast.makeText(applicationContext, "No sea seleccionado una imagen", Toast.LENGTH_LONG)
                    .show()
        } else if (reduceBitmap(applicationContext, DatosUsuario!!.imagen, 512f, 512f) != null) {
            Toast.makeText(applicationContext,
                    "Se cargo la imagen correctamente",
                    Toast.LENGTH_LONG).show()
            FotoUsuario!!.setImageBitmap(reduceBitmap(applicationContext,
                    DatosUsuario!!.imagen,
                    512f,
                    512f))
        } else {
            Toast.makeText(applicationContext, "La imagen no se encuentra", Toast.LENGTH_LONG)
                    .show()
        }
    }

    fun pasarBlunoConectar() {
        val siguiente = Intent(this@MenuPrincipal, ConectarBluno::class.java)
        startActivity(siguiente)
        finish()
    }

    fun openGallery(view: View?) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val db = DB(applicationContext)
        if (resultCode == RESULT_OK) {
            DatosUsuario?.imagen = data?.dataString!!
            if (db.updateImage(DatosUsuario!!)) {
                Toast.makeText(applicationContext, "Se guardo correctamente la URL de la imagen", Toast.LENGTH_LONG).show()
                FotoUsuario?.setImageBitmap(reduceBitmap(applicationContext, data.dataString, 512f, 512f))
            } else {
                Toast.makeText(applicationContext, "Ocurrio un error al guardar la URL de la imagen", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun Salir(item: MenuItem?) {
        val siguiente = Intent(this@MenuPrincipal, InicioSesion::class.java)
        startActivity(siguiente)
        finish()
    }
}