package com.example.estres2.actividades.bluetooth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.estres2.actividades.bluetooth.adapters.AdapterWearable
import com.example.estres2.MainActivity
import com.example.estres2.R
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.actividades.bluetooth.viewmodel.BlunoViewModel
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.archivo.RegisterFile
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.almacenamiento.entidades.wearable.Wearable
import com.example.estres2.databinding.ActivityBlunoBinding
import com.example.estres2.util.requestPermissionBluetooth
import java.io.File
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class Bluno : BlunoLibrary() {
    private lateinit var binding: ActivityBlunoBinding
    private lateinit var context: Context
    private lateinit var blue: BluetoothAdapter
    private lateinit var userObject: User
    private lateinit var fileWriter: FileWriter
    private val blunoViewModel: BlunoViewModel by viewModels()
    private var wearablesList: MutableList<Wearable> = ArrayList()
    private var stateMonitoring: Boolean = true
    private var character1 = 0
    private var character2 = 0
    private var characterIndex = 0
    private var finalValue = 0
    private var cellIndex = 1
    private lateinit var notification: NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = applicationContext
        setObserves()
        createNotificationChannel()
        initializeObjects()
        requestPermissionBluetooth(context, this)
        onCreateProcess() //onCreate Process by BlunoLibrary
    }

    override fun onResume() {
        super.onResume()
        println("BlUNO Activity onResume") //Toast.makeText(this, "BlUNO Activity onResume", Toast.LENGTH_LONG).show()
        onResumeProcess() //onResume Process by BlunoLibrary
    }

    override fun onPause() {
        super.onPause()
        println("BlUNO Activity onPause") //Toast.makeText(this, "BlUNO Activity onPause", Toast.LENGTH_LONG).show()
        onPauseProcess() //onPause Process by BlunoLibrary
    }

    override fun onStop() {
        super.onStop()
        println("BlUNO Activity onStop") //Toast.makeText(this, "BlUNO Activity onStop", Toast.LENGTH_LONG).show()
        onStopProcess() //onStop Process by BlunoLibrary
    }

    override fun onDestroy() {
        super.onDestroy()
        println("BlUNO Activity onDestroy") //Toast.makeText(this, "BlUNO Activity onDestroy", Toast.LENGTH_LONG).show()
        onDestroyProcess() //onDestroy Process by BlunoLibrary
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResultProcess(requestCode, resultCode) //onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onConectionStateChange(theConnectionState: connectionStateEnum) { //Once connection state changes, this function will be called
        binding.apply {
            when (theConnectionState) {
                connectionStateEnum.isConnected -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_state_connect)
                    controlBluno.isEnabled = true
                    blunoViewModel.showNotificationCommunicationIsConnected(true)
                }
                connectionStateEnum.isConnecting -> {
                    blunoViewModel.updateWearablesList(true)
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_state_connecting)
                    controlBluno.isEnabled = false
                }
                connectionStateEnum.isToScan -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_state_scan)
                    controlBluno.isEnabled = false
                    if(!stateMonitoring) {
                        stateMonitoring = true
                        controlBluno.setImageResource(R.drawable.ic_stop_monitoring)
                        buttonScanBlunoConected.isEnabled = true
                        CSRegresar.isEnabled = true
                        closeFile()
                        (controlBluno.drawable as Animatable).start()
                    }
                    blunoViewModel.showNotificationCommunicationIsConnected(false)
                }
                connectionStateEnum.isScanning -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_state_scanning)
                    controlBluno.isEnabled = false
                }
                connectionStateEnum.isDisconnecting -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_state_disconnecting)
                    controlBluno.isEnabled = false
                }
                else -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_state_not_connect)
                    controlBluno.isEnabled = false
                }
            }
        }
    }

    override fun onSerialReceived(theString: String) { //Once connection data received, this function will be called
        binding.apply {
            serialReveicedText.text = String.format("%s%s", serialReveicedText.text, theString) //append the text into the EditText
            try {
                organize(theString)
                Log.d("Escritura", "Se escribió correctamente")
            } catch (e: Exception) {
                Log.d("Escritura", "Ocurrió un error al ecribir")
            }
            (serialReveicedText.parent as ScrollView).fullScroll(View.FOCUS_DOWN) //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        }
    }

    // Se anula el botón que nos regresa
    override fun onBackPressed() {}

    private fun organize(filter: String) {
        filter.forEach { char ->
            if (char != '\n') {
                if (characterIndex == 0) {
                    character1 = when (char) {
                        '!' -> {
                            0
                        }
                        ' ' -> {
                            93
                        }
                        else -> {
                            char.toByte().toInt().minus(34)
                        }
                    }
                    finalValue = character1
                } else {
                    character2 = when (char) {
                        '!' -> {
                            0
                        }
                        ' ' -> {
                            93
                        }
                        else -> {
                            char.toByte().toInt().minus(34)
                        }
                    }
                    finalValue = character1*94 + character2
                }
                characterIndex++
            } else {
                fileWriter.append(finalValue.toString())
                fileWriter.append(",")
                cellIndex = if (cellIndex == 2) {
                    fileWriter.append("\n")
                    1
                } else {
                    cellIndex.plus(1)
                }
                character1 = 0
                character2 = 0
                characterIndex = 0
            }
        }
    }

    private fun setObserves() {
        blunoViewModel.apply {
            updateWearables.observe(this@Bluno) {
                if (it) {
                    getWearables(context)
                }
            }
        }
        blunoViewModel.apply {
            showNotificationCommunicationIsConnected.observe(this@Bluno) {
                if (it) {
                    setNotification("Conectado")
                } else {
                    setNotification("Desconectado")
                }
            }
        }
    }

    private fun initializeObjects() {
        userObject = getObjectBoleta()
        serialBegin(115200) //set the Uart Baudrate on BLE chip to 115200
        val learningUnit = arrayOf("Selecciona una materia", "Líneas de Transmisión y Antenas", "Teoría de la Información",
                "Teoría de las Comunicaciones", "Variable Compleja", "Protocolos de Internet", "Comunicaciones Digítales",
                "Sistemas Distribuidos", "Metodología", "Sistemas Celulares", "Multimedia", "Señales y Sistemas", "Probabilidad",
                "Programación de Dispositivos Móviles", "PT1", "PT2")
        val adapterUnit = ArrayAdapter(this, R.layout.spinner_custom, learningUnit)
        getWearables(this)

        binding.apply {
            controlBluno.isEnabled = false
            CMMateria.adapter = adapterUnit
            blue = BluetoothAdapter.getDefaultAdapter()
            buttonScanBlunoConected.setOnClickListener {
                //Alert Dialog for selecting the BLE device
                if (!blue.isEnabled) {
                    blue.enable()
                    SystemClock.sleep(350)
                }
                buttonScanOnClickProcess() //Alert Dialog for selecting the BLE device
            }
            controlBluno.setOnClickListener {
                animation()
            }
            CSRegistro.setOnClickListener {
                exportCSV()
            }
            CSRegresar.setOnClickListener {
                back()
            }
            // Este método puede eliminarse una vez se tenga la comprobación de la transmisión
            buttonSerialSend.setOnClickListener {
                serialSend(serialSendText.text.toString()) //send the data to the BLUNO
            }
        }
    }

    private fun exportCSV() {
        val hourDateFormat: DateFormat = SimpleDateFormat("HH_mm_ss_dd_MM_yyyy", Locale.US)
        val mainFolder = File(Environment.getExternalStorageDirectory().path + "/Monitoreo" + userObject.boleta)
        val db = DB(context)
        Log.d("Hora", hourDateFormat.format(Date()))
        val file = mainFolder.toString() + "/" + userObject.boleta + "_" + hourDateFormat.format(Date()).trim { it <= ' ' } + ".csv"
        if (db.insertRecord(RegisterFile(userObject.boleta + "_" + hourDateFormat.format(Date()).trim { it <= ' ' } + ".csv", userObject.boleta))) {
            try {
                fileWriter = FileWriter(file)
                fileWriter.append(userObject.boleta).append("\n")
                fileWriter.append(binding.CMMateria.selectedItem.toString()).append("\n")
                fileWriter.append(hourDateFormat.format(Date())).append("\n")
                fileWriter.append("4, 4").append("\n")
                Toast.makeText(this, "Se creó correctmente el registro de las variables.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("Exception_FillWriter", e.toString())
            }
        } else {
            println("No se insertó correctamente el registro a la base de datos.") //Toast.makeText(this, "No se insertó correctamente el registro a la base de datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animation() {
        binding.apply {
            if (CMMateria.selectedItem.toString() != "Selecciona una materia") {
                if (stateMonitoring) {
                    stateMonitoring = false
                    controlBluno.setImageResource(R.drawable.ic_start_monitoring)
                    serialSend("P")
                    buttonScanBlunoConected.isEnabled = false
                    CSRegresar.isEnabled = false
                    exportCSV()
                } else {
                    stateMonitoring = true
                    controlBluno.setImageResource(R.drawable.ic_stop_monitoring)
                    serialSend("S")
                    buttonScanBlunoConected.isEnabled = true
                    CSRegresar.isEnabled = true
                    closeFile()
                }
                (controlBluno.drawable as Animatable).start()
            } else {
                Toast.makeText(applicationContext, "No se ha seleccionado una materia", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Función en la que obtenemos los parametros2
    private fun getWearables(context: Context) {
        val bd = DB(context)
        wearablesList = bd.showWearable() as MutableList<Wearable>
        if (wearablesList.isEmpty())
            wearablesList.add(Wearable("Wearable no registrado", "sin mac"))
        val adapter = AdapterWearable(wearablesList, blunoViewModel)
        binding.apply {
            rvBlunoMostrarWearables.layoutManager = GridLayoutManager(context, 1)
            rvBlunoMostrarWearables.adapter = adapter
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

    private fun setNotification(contentNotification: String) {
        println("Entró a la notificación")
        notification = NotificationCompat.Builder(applicationContext, CHANNEL_0_ID).apply {
            setContentTitle("Estado Conexión")
            setContentText(contentNotification)
            setSubText("Conexión")
            setSmallIcon(R.drawable.ic_state_scan)
            color = ContextCompat.getColor(applicationContext, R.color.colorPrimary)
            priority = NotificationCompat.PRIORITY_HIGH
        }
        NotificationManagerCompat.from(applicationContext).apply {
            notify(NOTIFICATION_0, notification.build())
        }
    }

    private fun closeFile() {
        try {
            fileWriter.close()
            println("Se cerró correctmente el registro de las variables.") //Toast.makeText(applicationContext, "Se cerró correctmente el registro de las variables.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            println("Hubo un problema al cerrar el registro de las variables.") //Toast.makeText(applicationContext, "Hubo un problema al cerrar el registro de las variables.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun back() {
        closeFile()
        startActivity(Intent(this@Bluno, MainActivity::class.java))
        finish()
    }

    companion object {
        const val CHANNEL_0_ID = "Channel_1"
        const val NOTIFICATION_0 = 1
    }
}