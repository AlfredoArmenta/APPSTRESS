package com.example.estres2.actividades.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.io.File
import java.io.FileWriter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ConectarBluno : BlunoLibrary() {
    private lateinit var buttonScan: ImageButton
    private lateinit var serialSendText: EditText
    private lateinit var serialReceivedText: TextView
    private lateinit var estadoMonitoreo: ImageButton
    private var ObjetoUsuario: Usuario? = null
    private lateinit var rvWearable: RecyclerView
    private var wearablesLista: MutableList<Wearable> = ArrayList()
    var eliminarWearable: EditText? = null
    var btn_eliminar: Button? = null
    private var fileWriter: FileWriter? = null
    private lateinit var UA: Spinner
    private lateinit var blue: BluetoothAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluno)
        inicializarVariables()
        pedirPermisos()
        onCreateProcess() //onCreate Process by BlunoLibrary
    }

    fun inicializarVariables() {
        ObjetoUsuario = getObjectBoleta()
        estadoMonitoreo = findViewById(R.id.control_bluno)
        estadoMonitoreo.isEnabled = false
        estadoMonitoreo.isSelected = true
        serialBegin(115200) //set the Uart Baudrate on BLE chip to 115200
        serialReceivedText =
                findViewById(R.id.serialReveicedText) //initial the EditText of the received data
        serialSendText =
                findViewById(R.id.serialSendText) //initial the EditText of the sending data
        UA = findViewById(R.id.CMMateria)
        val UnidadAprendizaje = arrayOf("Selecciona una materia",
                "Líneas de Transmisión y Antenas",
                "Teoria de la Informacion",
                "Teoria de las Comunicaciones",
                "Variable Compleja",
                "Protocolos de Internet",
                "Comunicaciones Digitales",
                "Sistemas Distribuidos",
                "Metodologia",
                "Sistemas Celulares",
                "Multimedia",
                "Señales y Sistemas",
                "Probabilidad",
                "Programacion de Dispositivos Moviles",
                "PT1",
                "PT2")
        val AdapterUnidad =
                ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, UnidadAprendizaje)
        UA.setAdapter(AdapterUnidad)
        eliminarWearable = findViewById(R.id.Eliminar_Wearable)
        btn_eliminar = findViewById(R.id.boton_eliminar)
        rvWearable = findViewById(R.id.rvBluno_Mostrar_Wearables)
        rvWearable.layoutManager = GridLayoutManager(this, 1)
        blue = BluetoothAdapter.getDefaultAdapter()
        obtenerWearables(this)
        val buttonSerialSend =
                findViewById<Button>(R.id.buttonSerialSend) //initial the button for sending the data
        val regresar = findViewById<Button>(R.id.CSRegresar)
        regresar.setOnClickListener { Salir() }
        buttonSerialSend.setOnClickListener {
            // TODO Auto-generated method stub
            serialSend(serialSendText.text.toString()) //send the data to the BLUNO
        }
        buttonScan = findViewById(R.id.buttonScanBlunoConected) //initial the button for scanning the BLE device
        buttonScan.setOnClickListener {
            // TODO Auto-generated method stub
            //Alert Dialog for selecting the BLE device
            if (!blue.isEnabled()) {
                blue.enable()
                SystemClock.sleep(350)
            }
            buttonScanOnClickProcess() //Alert Dialog for selecting the BLE device
        }
    }

    fun pedirPermisos() {
        // Permisos para bluetooth
        val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                Toast.makeText(this,
                        "Se requiere permiso para obtener datos de ubicación BLE",
                        Toast.LENGTH_SHORT).show()
            } else {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }
        } else {
            Toast.makeText(this, """
     
     Permisos de ubicación ya otorgados
     """.trimIndent(), Toast.LENGTH_SHORT).show()
        }
    }

    fun ExportarCSV(view: View?) {
        val hourdateFormat: DateFormat = SimpleDateFormat("HH_mm_ss_dd_MM_yyyy")
        val Carpeta = File(Environment.getExternalStorageDirectory().path + "/Monitoreo" + ObjetoUsuario?.boleta)
        Toast.makeText(this, Carpeta.path, Toast.LENGTH_SHORT).show()
        var isCreate = false
        if (!Carpeta.exists()) {
            isCreate = Carpeta.mkdir()
            Toast.makeText(this, "Se creo la carpeta", Toast.LENGTH_SHORT).show()
        }
        Log.d("Hora", hourdateFormat.format(Date()))
        val Archivo =
                Carpeta.toString() + "/" + ObjetoUsuario!!.boleta + "_" + hourdateFormat.format(
                        Date()).trim { it <= ' ' } + ".csv"
        val db = DB(applicationContext)
        if (db.insertRecord(Archivo(ObjetoUsuario!!.boleta + "_" + hourdateFormat.format(Date())
                        .trim { it <= ' ' } + ".csv", ObjetoUsuario!!.boleta))
        ) {
            try {
                fileWriter = FileWriter(Archivo)
                fileWriter!!.append(ObjetoUsuario!!.boleta).append("\n")
                fileWriter!!.append(UA!!.selectedItem.toString()).append("\n")
                fileWriter!!.append(hourdateFormat.format(Date())).append("\n")
                fileWriter!!.append("MuestraFC, TiempoFC, MuestraGSR, TiempoGSR").append("\n")
                Toast.makeText(this,
                        "Se creó correctmente el registro de las variables.",
                        Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.d("Exception_FillWriter", e.toString())
            }
        } else {
            Toast.makeText(this,
                    "No se inserto correctamente el registro a la base de datos",
                    Toast.LENGTH_SHORT).show()
        }
    }

    fun animar(view: View?) {
        if (UA!!.selectedItem.toString() != "Selecciona una materia") {
            estadoMonitoreo!!.isSelected = !estadoMonitoreo!!.isSelected
            if (!estadoMonitoreo!!.isSelected) {
                estadoMonitoreo!!.setImageResource(R.drawable.ic_detener_monitoreo)
                serialSend("Play")
                ExportarCSV(view)
            } else {
                estadoMonitoreo!!.setImageResource(R.drawable.ic_comienza_monitoreo)
                serialSend("Stop")
                try {
                    fileWriter!!.append("Fin del registro")
                    fileWriter!!.close()
                    Toast.makeText(this,
                            "Se cerró correctmente el registro de las variables.",
                            Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(this,
                            "Hubo un problema al cerrar el registro de las variables.",
                            Toast.LENGTH_SHORT).show()
                }
            }
            (estadoMonitoreo!!.drawable as Animatable).start()
        } else {
            Toast.makeText(this, "No se ha seleccionado una materia", Toast.LENGTH_LONG).show()
        }
    }

    // Función en la que obtenemos los parametros2
    fun obtenerWearables(context: Context?) {
        val bd = DB(context!!)
        wearablesLista = bd.showWearable() as MutableList<Wearable>
        if (wearablesLista.isEmpty())
            wearablesLista.add(Wearable("Wearable no registrado", "sin mac"))
        val adapter = AdapterWearable(context, wearablesLista)
        rvWearable!!.layoutManager = GridLayoutManager(context, 1)
        rvWearable!!.adapter = adapter
    }

    fun eliminarWearable(view: View?) {
        val bd = DB(applicationContext)
        if (bd.deleteWearable(eliminarWearable!!.text.toString())) {
            Toast.makeText(this, "Se Elimino correctamente el Wearable", Toast.LENGTH_LONG).show()
            obtenerWearables(this)
        } else {
            Toast.makeText(this, "No se Borro ni madres", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "BlUNO Activity onResume", Toast.LENGTH_LONG).show()
        println("BlUNOActivity onResume")
        onResumeProcess() //onResume Process by BlunoLibrary
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResultProcess(requestCode, resultCode) //onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "BlUNO Activity onPause", Toast.LENGTH_LONG).show()
        onPauseProcess() //onPause Process by BlunoLibrary
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "BlUNO Activity onStop", Toast.LENGTH_LONG).show()
        onStopProcess() //onStop Process by BlunoLibrary
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "BlUNO Activity onDestroy", Toast.LENGTH_LONG).show()
        onDestroyProcess() //onDestroy Process by BlunoLibrary
    }

    override fun onConectionStateChange(theConnectionState: connectionStateEnum) { //Once connection state changes, this function will be called
        when (theConnectionState) {
            connectionStateEnum.isConnected -> {
                buttonScan!!.setImageResource(R.drawable.ic_estado_conectado)
                estadoMonitoreo!!.isEnabled = true
            }
            connectionStateEnum.isConnecting -> {
                btn_eliminar!!.isEnabled = false
                obtenerWearables(this)
                buttonScan!!.setImageResource(R.drawable.ic_estado_conectando)
                estadoMonitoreo!!.isEnabled = false
            }
            connectionStateEnum.isToScan -> {
                btn_eliminar!!.isEnabled = true
                buttonScan!!.setImageResource(R.drawable.ic_estado_scan)
                estadoMonitoreo!!.isEnabled = false
            }
            connectionStateEnum.isScanning -> {
                buttonScan!!.setImageResource(R.drawable.ic_estado_is_scanning)
                estadoMonitoreo!!.isEnabled = false
            }
            connectionStateEnum.isDisconnecting -> {
                buttonScan!!.setImageResource(R.drawable.ic_estado_desconectando)
                estadoMonitoreo!!.isEnabled = false
            }
            else -> {
                buttonScan!!.setImageResource(R.drawable.ic_estado_no_conectado)
                estadoMonitoreo!!.isEnabled = false
            }
        }
    }

    override fun onSerialReceived(theString: String) {                            //Once connection data received, this function will be called
        // TODO Auto-generated method stub
        serialReceivedText!!.text = String.format("%s%s",
                serialReceivedText!!.text,
                theString) //append the text into the EditText
        try {
            fileWriter!!.write(theString)
            Log.d("Escritura", "Se escribió correctamente")
        } catch (e: Exception) {
            Log.d("Escritura", "Ocurrio un error al ecribir")
        }
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        (serialReceivedText!!.parent as ScrollView).fullScroll(View.FOCUS_DOWN)
    }

    fun Salir() {
        try {
            fileWriter!!.close()
        } catch (e: Exception) {
            Log.d("Cierre de archivo", e.toString())
        }
        val siguiente = Intent(this@ConectarBluno, MainActivity::class.java)
        startActivity(siguiente)
        finish()
    }

    // Se anula el botón que nos regresa
    override fun onBackPressed() {}
}