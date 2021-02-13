package com.example.estres2.actividades.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.AdapterWearable
import com.example.estres2.MainActivity
import com.example.estres2.R
import com.example.estres2.UsuarioBoleta.getObjectBoleta
import com.example.estres2.almacenamiento.database.DB
import com.example.estres2.almacenamiento.entidades.archivo.Archivo
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.almacenamiento.entidades.wearable.Wearable
import com.example.estres2.databinding.ActivityBlunoBinding
import com.example.estres2.util.requestPermissionBluetooth
import java.io.File
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ConectarBluno : BlunoLibrary() {
    private lateinit var binding: ActivityBlunoBinding
    private lateinit var context: Context
    private lateinit var blue: BluetoothAdapter
    private lateinit var userObject: Usuario
    private var wearablesList: MutableList<Wearable> = ArrayList()
    private var fileWriter: FileWriter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlunoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = applicationContext
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
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_estado_conectado)
                    controlBluno.isEnabled = true
                }
                connectionStateEnum.isConnecting -> {
                    botonEliminar.isEnabled = false
                    getWearables(context)
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_estado_conectando)
                    controlBluno.isEnabled = false
                }
                connectionStateEnum.isToScan -> {
                    botonEliminar.isEnabled = true
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_estado_scan)
                    controlBluno.isEnabled = false
                }
                connectionStateEnum.isScanning -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_estado_is_scanning)
                    controlBluno.isEnabled = false
                }
                connectionStateEnum.isDisconnecting -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_estado_desconectando)
                    controlBluno.isEnabled = false
                }
                else -> {
                    buttonScanBlunoConected.setImageResource(R.drawable.ic_estado_no_conectado)
                    controlBluno.isEnabled = false
                }
            }
        }
    }

    override fun onSerialReceived(theString: String) { //Once connection data received, this function will be called
        binding.apply {
            serialReveicedText.text = String.format("%s%s", serialReveicedText.text, theString) //append the text into the EditText
            try {
                fileWriter!!.write(theString)
                Log.d("Escritura", "Se escribió correctamente")
            } catch (e: Exception) {
                Log.d("Escritura", "Ocurrio un error al ecribir")
            }
            (serialReveicedText.parent as ScrollView).fullScroll(View.FOCUS_DOWN) //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        }
    }

    private fun initializeObjects() {
        userObject = getObjectBoleta()
        serialBegin(115200) //set the Uart Baudrate on BLE chip to 115200
        val learningUnit = arrayOf("Selecciona una materia", "Líneas de Transmisión y Antenas", "Teoria de la Informacion",
                "Teoria de las Comunicaciones", "Variable Compleja", "Protocolos de Internet", "Comunicaciones Digitales",
                "Sistemas Distribuidos", "Metodologia", "Sistemas Celulares", "Multimedia", "Señales y Sistemas", "Probabilidad",
                "Programacion de Dispositivos Moviles", "PT1", "PT2")
        val adapterUnit = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, learningUnit)
        getWearables(this)

        binding.apply {
            controlBluno.isEnabled = false
            controlBluno.isSelected = true
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
            botonEliminar.setOnClickListener {
             eraserWearable()
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
        val hourDateFormat: DateFormat = SimpleDateFormat("HH_mm_ss_dd_MM_yyyy")
        val folder = File(Environment.getExternalStorageDirectory().path + "/Monitoreo" + userObject.boleta)
        val db = DB(context)
        if (!folder.exists()) {
            folder.mkdir()
            Toast.makeText(this, "Se creo la carpeta", Toast.LENGTH_SHORT).show()
        }
        println(folder.path)
        Log.d("Hora", hourDateFormat.format(Date()))
        val file = folder.toString() + "/" + userObject.boleta + "_" + hourDateFormat.format(Date()).trim { it <= ' ' } + ".csv"
        if (db.insertRecord(Archivo(userObject.boleta + "_" + hourDateFormat.format(Date()).trim { it <= ' ' } + ".csv", userObject.boleta))) {
            try {
                fileWriter = FileWriter(file)
                fileWriter?.append(userObject.boleta)?.append("\n")
                fileWriter?.append(binding.CMMateria.selectedItem.toString())?.append("\n")
                fileWriter?.append(hourDateFormat.format(Date()))?.append("\n")
                fileWriter?.append("MuestraFC, TiempoFC, MuestraGSR, TiempoGSR")?.append("\n")
                Toast.makeText(this, "Se creó correctmente el registro de las variables.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("Exception_FillWriter", e.toString())
            }
        } else {
            Toast.makeText(this, "No se inserto correctamente el registro a la base de datos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animation() {
        binding.apply {
            if (CMMateria.selectedItem.toString() != "Selecciona una materia") {
                controlBluno.isSelected = !controlBluno.isSelected
                if (controlBluno.isSelected) {
                    controlBluno.setImageResource(R.drawable.ic_detener_monitoreo)
                    serialSend("Play")
                    exportCSV()
                } else {
                    controlBluno.setImageResource(R.drawable.ic_comienza_monitoreo)
                    serialSend("Stop")
                    try {
                        fileWriter?.append("Fin del registro")
                        fileWriter?.close()
                        Toast.makeText(applicationContext, "Se cerró correctmente el registro de las variables.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Hubo un problema al cerrar el registro de las variables.", Toast.LENGTH_SHORT).show()
                    }
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
        val adapter = AdapterWearable(context, wearablesList)
        binding.apply {
            rvBlunoMostrarWearables.layoutManager = GridLayoutManager(context, 1)
            rvBlunoMostrarWearables.adapter = adapter
        }
    }

    private fun eraserWearable() {
        val bd = DB(applicationContext)
        if (bd.deleteWearable(binding.EliminarWearable.text.toString())) {
            Toast.makeText(this, "Se Elimino correctamente el Wearable", Toast.LENGTH_LONG).show()
            getWearables(this)
        } else {
            Toast.makeText(this, "No se Borro ni madres", Toast.LENGTH_LONG).show()
        }
    }

    private fun back() {
        try { fileWriter?.close()
        } catch (e: Exception) {
            Log.d("Cierre de archivo", e.toString())
        }
        startActivity(Intent(this@ConectarBluno, MainActivity::class.java))
        finish()
    }

    // Se anula el botón que nos regresa
    override fun onBackPressed() {}
}