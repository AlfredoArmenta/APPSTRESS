package com.example.estres2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.actividades.bluetooth.Bluno
import com.example.estres2.actividades.iniciosesion.Login
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.ActivityMainBinding
import com.example.estres2.util.*
import kotlinx.coroutines.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var hView: View
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var userName: TextView
    private lateinit var userBoleta: TextView
    private lateinit var userData: User
    private lateinit var userImage: ImageView
    private lateinit var addImageView: ImageButton
    private lateinit var notification: NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        initializeObjects()
        setObservers()
    }

    private fun setObservers() {
        mainViewModel.apply {
            updateUserData.observe(this@MainActivity) {
                if (it) {
                    userData = getObjectBoleta()
                    userName.text = userData.nombre
                    userBoleta.text = userData.boleta
                    println("Se actualizó el Usuario")
                }
            }
            updateNotificationAnalysis.observe(this@MainActivity) { status ->
                if (status) {
                    setNotification("Analisis")
                    analysisSampEn(false)
                    binding.appBarMenu.fab.isEnabled = false
                    println("Notificación Analisis")
                }
            }

            updateNotificationGraph.observe(this@MainActivity) { state ->
                if (state) {
                    setNotification("Graficación")
                    graphSampEn()
                    binding.appBarMenu.fab.isEnabled = false
                    println("Notificación Gráficar")
                }
            }

            updateNotificationAnalysisAndGraph.observe(this@MainActivity) { state ->
                if (state) {
                    setNotification("Analizando y Graficando")
                    analysisSampEn(true)
                    binding.appBarMenu.fab.isEnabled = false
                    println("Notificación Analisis y Gráficar")
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
                    R.id.nav_inicio, R.id.nav_registro,
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

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Analysis Notification"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_0_ID, name, importance).apply {
                description = descriptionText
                titleColor = Color.RED
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNotification(titleNotification: String) {
        notification = NotificationCompat.Builder(this, CHANNEL_0_ID).apply {
            setContentTitle(titleNotification)
            setContentText("Leyendo Archivo")
            setSubText("Estimación")
            setSmallIcon(R.drawable.ic_login)
            color = Color.GRAY
            priority = NotificationCompat.PRIORITY_LOW
            setProgress(0, 0, true)
        }
        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICATION_0, notification.build())
        }
    }

    private fun analysisSampEn(graph: Boolean) {
        lifecycleScope.launch(context = Dispatchers.Main) {
            withContext(context = Dispatchers.IO) {
                readRegister()
                NotificationManagerCompat.from(applicationContext).apply {
                    notification.setContentText("Lectura Terminada")
                    notify(NOTIFICATION_0, notification.build())
                }
            }

            val fcCoroutine = async(Dispatchers.IO) {
                println("*****************FC**************************")
                EntropyObject.setEntropy(sampEn(FileCharacteristics.getFc(), 3, 0.2))
                1
            }

            val gsrCoroutine = async(Dispatchers.IO) {
                println("*****************GSR**************************")
                EntropyObject.setEntropy(sampEn(FileCharacteristics.getGsr(), 3, 0.2))
                1
            }

            if ((fcCoroutine.await() + gsrCoroutine.await()) >= 0) {
                NotificationManagerCompat.from(applicationContext).apply {
                    notification.setContentText("Completado. Tu estado actual es: Estresado")
                    notification.setProgress(0, 0, false)
                    notify(NOTIFICATION_0, notification.build())
                }
                mainViewModel.updateGraph(graph)
                binding.appBarMenu.fab.isEnabled = true
            }
        }
    }

    private fun graphSampEn() {
        lifecycleScope.launch(context = Dispatchers.Main) {
            withContext(context = Dispatchers.IO) {
                readRegister()
                NotificationManagerCompat.from(applicationContext).apply {
                    notification.setContentText("Completado")
                    notification.setProgress(0, 0, false)
                    notify(NOTIFICATION_0, notification.build())
                }
            }
            mainViewModel.updateGraph(true)
        }
    }

    private fun goToBluno() {
        startActivity(Intent(this@MainActivity, Bluno::class.java))
        finish()
    }

    private fun openGallery() {
        println("Se entró a la función openGallery")
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/"
        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), 10)
    }

    private fun backLogging() {
        startActivity(Intent(this@MainActivity, Login::class.java))
        finish()
    }

    companion object {
        const val CHANNEL_0_ID = "Channel_0"
        const val NOTIFICATION_0 = 0
    }
}