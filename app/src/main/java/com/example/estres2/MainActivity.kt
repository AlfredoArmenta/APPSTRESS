package com.example.estres2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
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
import com.example.estres2.util.reduceBitmap
import com.example.estres2.databinding.ActivityMainBinding
import com.example.estres2.util.FileObject
import com.example.estres2.util.requestPermissionExternalStorage
import com.example.estres2.util.sampEn
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import kotlinx.coroutines.*
import java.io.File
import java.io.FileReader

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

    private var fc: MutableList<Double> = ArrayList()
    private var fcTime: MutableList<Double> = ArrayList()
    private var gsr: MutableList<Double> = ArrayList()
    private var gsrTime: MutableList<Double> = ArrayList()

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
            updateNotification.observe(this@MainActivity) { status ->
                if (status) {
                    setNotification()
                }
            }

            updateStateNotification.observe(this@MainActivity) { state ->
                if (state) {
                    NotificationManagerCompat.from(applicationContext).apply {
                        notification.setContentText("Analisis Finalizado")
                        notification.setProgress(0, 0, false)
                        notify(NOTIFICATION_0, notification.build())
                    }
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

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Analysis Notification"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_0_ID, name, importance).apply {
                description = descriptionText
                titleColor = Color.RED
            }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setNotification() {
        notification = NotificationCompat.Builder(this, CHANNEL_0_ID).apply {
            setContentTitle("Analisis 0%")
            setContentText("Leyendo Archivo")
            setSubText("Estimación")
            setSmallIcon(R.drawable.ic_login)
            color = Color.MAGENTA
            priority = NotificationCompat.PRIORITY_LOW
            setProgress(0, 0,true)
        }
        NotificationManagerCompat.from(this).apply {
            notify(NOTIFICATION_0, notification.build())
        }
        lifecycleScope.launch(context = Dispatchers.Main) {
            val read = withContext(context = Dispatchers.IO) {
                readRegister("ACC.csv")
                NotificationManagerCompat.from(applicationContext).apply {
                    notification.setContentTitle("Analisis 50%")
                    notification.setContentText("Analisis Iniciado")
                    notify(NOTIFICATION_0, notification.build())
                }
            }

            val fcCoroutine = async(Dispatchers.IO) {
                println("*****************FC**************************")
                sampEn(fc, 3, 0.2)
            }

            val gsrCoroutine = async(Dispatchers.IO) {
                println("*****************GSR**************************")
                sampEn(gsr, 3, 0.2)
            }

            if((fcCoroutine.await() + gsrCoroutine.await()) >= 0){
                NotificationManagerCompat.from(applicationContext).apply {
                    notification.setContentTitle("Analisis 100%")
                    notification.setContentText("Analisis Terminado")
                    notification.setProgress(0, 0, false)
                    notify(NOTIFICATION_0, notification.build())
                }
            }
        }
    }

    private fun readRegister(nameRegister: String){
        lateinit var boleta: String
        lateinit var materia: String
        lateinit var fecha: String
//        val fc: MutableList<String> = ArrayList()
//        val fcTime: MutableList<String> = ArrayList()
//        val gsr: MutableList<String> = ArrayList()
//        val gsrTime: MutableList<String> = ArrayList()
        try {
            val file = FileReader(File(Environment.getExternalStorageDirectory().toString() + "/Monitoreo" + getObjectBoleta().boleta + "/ACC.csv"))
            val parse = CSVParserBuilder().withSeparator(',').build()
            val cvsReader = CSVReaderBuilder(file)
                    .withCSVParser(parse)
                    .build()

            val lines = cvsReader.readAll()

            var i = 0

            for (row in lines) {
                var j = 0
                for (cell in row) {
                    when (i) {
                        0 -> {
                            boleta = cell
                        }
                        1 -> {
                            materia = cell
                        }
                        2 -> {
                            fecha = cell
                        }
                        else -> {
                            when (j) {
//                                0 -> {
//                                    fc.add(cell)
//                                }
//                                1 -> {
//                                    fcTime.add(cell)
//                                }
//                                2 -> {
//                                    gsr.add(cell)
//                                }
//                                else -> {
//                                    gsrTime.add(cell)
//                                }
                                0 -> {
                                    fc.add(cell.toDouble())
                                }
                                1 -> {
                                    fcTime.add(cell.toDouble())
                                }
                                2 -> {
                                    gsr.add(cell.toDouble())
                                }
                                else -> {
                                    gsrTime.add(cell.toDouble())
                                }
                            }
                        }
                    }
                    j += 1
                }
                i += 1
            }
        } catch (e: Exception) {
            e.printStackTrace();
        } finally {
            NotificationManagerCompat.from(this).apply {
                notification.setContentTitle("Analisis 50%")
                notification.setContentText("Lectura Terminada")
                notify(NOTIFICATION_0, notification.build())
            }
            println(" ____________ CSV Read Finished ____________ ")
        }


            println("Boleta: $boleta")
            println("Materia: $materia")
            println("Fecha: $fecha")
            println("FC: ${fc.first()} Long: ${fc.size}")
            println("FCTIME: Long: ${fcTime.size}")
            println("GSR: Long: ${gsr.size}")
            println("GSRTIME: Long: ${gsrTime.size}")
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
        const val MAX_PROGRESS = 100
    }
}