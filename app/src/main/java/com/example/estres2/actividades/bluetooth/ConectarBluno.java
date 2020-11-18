package com.example.estres2.actividades.bluetooth;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.estres2.AdapterWearable;
import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;
import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.almacenamiento.entidades.wearable.Wearable;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConectarBluno extends BlunoLibrary {

    private ImageButton buttonScan;
    private EditText serialSendText;
    private TextView serialReceivedText;
    private ImageButton estadoMonitoreo;
    private String BoletaRecibida;
    private RecyclerView rvWearable;
    private List<Wearable> wearablesLista = new ArrayList<>();
    public EditText eliminarWearable;
    public Button btn_eliminar;

    // Variables globales para la generación del archivo del registro
    private Spinner UA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluno);

        inicializarVariables();
        recibirBoleta();
        pedirPermisos();
        onCreateProcess();                                                        //onCreate Process by BlunoLibrary
    }

    public void recibirBoleta() {
        // Se inicializa BoletaRecibida
        BoletaRecibida = "";

        // Se genera un objeto Bundle para poder recibir los parametros entre actividades
        Bundle BoletaR = getIntent().getExtras();

        //  Se pregunta si BoletaR es distinta de null lo que quiere decir que se recibio sin ningún problema
        if (BoletaR != null) {
            // Se obtiene la boleta
            BoletaRecibida = BoletaR.getString("Boleta");
            Toast.makeText(getApplicationContext(), "Boleta recibida: " + BoletaRecibida, Toast.LENGTH_SHORT).show();
        } else {
            finish();
        }
    }

    public void inicializarVariables() {
        estadoMonitoreo = findViewById(R.id.control_bluno);
        estadoMonitoreo.setEnabled(false);
        serialBegin(115200);                                                    //set the Uart Baudrate on BLE chip to 115200

        serialReceivedText = findViewById(R.id.serialReveicedText);    //initial the EditText of the received data
        serialSendText = findViewById(R.id.serialSendText);            //initial the EditText of the sending data

        UA = (Spinner) findViewById(R.id.CMMateria);

        String[] UnidadAprendizaje = {"Selecciona una materia", "Líneas de Transmisión y Antenas", "Teoria de la Informacion", "Teoria de las Comunicaciones", "Variable Compleja",
                "Protocolos de Internet", "Comunicaciones Digitales", "Sistemas Distribuidos", "Metodologia", "Sistemas Celulares", "Multimedia", "Señales y Sistemas", "Probabilidad",
                "Programacion de Dispositivos Moviles", "PT1", "PT2"};
        ArrayAdapter<String> AdapterUnidad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, UnidadAprendizaje);
        UA.setAdapter(AdapterUnidad);

        eliminarWearable = findViewById(R.id.Eliminar_Wearable);
        btn_eliminar = findViewById(R.id.boton_eliminar);
        rvWearable = findViewById(R.id.rvBluno_Mostrar_Wearables);
        rvWearable.setLayoutManager(new GridLayoutManager(this, 1));
        obtenerWearables(this);

        Button buttonSerialSend = findViewById(R.id.buttonSerialSend);        //initial the button for sending the data
        Button regresar = findViewById(R.id.CSRegresar);

        regresar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Salir();
            }
        });

        buttonSerialSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                serialSend(serialSendText.getText().toString());                //send the data to the BLUNO
            }
        });

        buttonScan = findViewById(R.id.buttonScanBlunoConected);                    //initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                buttonScanOnClickProcess();                                        //Alert Dialog for selecting the BLE device
            }
        });
    }

    public void pedirPermisos() {
        // Permisos para bluetooth
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Se requiere permiso para obtener datos de ubicación BLE", Toast.LENGTH_SHORT).show();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            Toast.makeText(this, "\n" +
                    "Permisos de ubicación ya otorgados", Toast.LENGTH_SHORT).show();
        }
    }

    public void ExportarCSV(View view) {

        DateFormat hourdateFormat = new SimpleDateFormat("HH_mm_ss_dd_MM_yyyy");
        File Carpeta = new File(Environment.getExternalStorageDirectory() + "/Monitoreo" + BoletaRecibida);

        boolean isCreate = false;

        if (!Carpeta.exists()) {
            isCreate = Carpeta.mkdir();
        }

        Log.d("Hora", hourdateFormat.format(new Date()));

        String Archivo = Carpeta.toString() + "/" + BoletaRecibida + "_" + hourdateFormat.format(new Date()).trim() + ".csv";

        try {
            FileWriter fileWriter = new FileWriter(Archivo);
            fileWriter.append(BoletaRecibida).append("\n");
            fileWriter.append(UA.getSelectedItem().toString()).append("\n");
            fileWriter.append(hourdateFormat.format(new Date())).append("\n");
            fileWriter.append("MuestraFC, TiempoFC, MuestraGSR, TiempoGSR");
            fileWriter.close();
            Toast.makeText(this, "Se creó correctmente el registro de las variables.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d("Exception_FillWriter", e.toString());
        }
    }

    public void animar(View view) {
        if (!UA.getSelectedItem().toString().equals("Selecciona una materia")) {
            estadoMonitoreo.setSelected(!estadoMonitoreo.isSelected());
            if (!estadoMonitoreo.isSelected()) {
                estadoMonitoreo.setImageResource(R.drawable.ic_detener_monitoreo);
                serialSend("Play");
            } else {
                estadoMonitoreo.setImageResource(R.drawable.ic_comienza_monitoreo);
                serialSend("Stop");
            }
            ((Animatable) estadoMonitoreo.getDrawable()).start();
        } else {
            Toast.makeText(this, "No se ha seleccionado una materia", Toast.LENGTH_LONG).show();
        }
    }

    // Función en la que obtenemos los parametros2
    public void obtenerWearables(Context context) {
        DB bd = new DB(context);
        wearablesLista.clear();
        wearablesLista = bd.MostrarWearable();
        if (wearablesLista.isEmpty())
            wearablesLista.add(new Wearable("Wearable no registrado","sin mac"));
        AdapterWearable adapter = new AdapterWearable(context, wearablesLista);
        rvWearable.setLayoutManager(new GridLayoutManager(context, 1));
        rvWearable.setAdapter(adapter);
    }

    public void eliminarWearable(View view) {
        DB bd = new DB(getApplicationContext());
        if (bd.BorrarWearable(eliminarWearable.getText().toString()) > 0) {
            Toast.makeText(this, "Se Elimino correctamente el Wearable", Toast.LENGTH_LONG).show();
            obtenerWearables(this);
        } else {
            Toast.makeText(this, "No se Borro ni madres", Toast.LENGTH_LONG).show();
        }
    }

    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "BlUNO Activity onResume", Toast.LENGTH_LONG).show();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();                                                        //onResume Process by BlunoLibrary
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode);                    //onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "BlUNO Activity onPause", Toast.LENGTH_LONG).show();
        onPauseProcess();                                                        //onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "BlUNO Activity onStop", Toast.LENGTH_LONG).show();
        onStopProcess();                                                        //onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        //serialSend("F"); //Finish Terminar la comunicación
        super.onDestroy();
        Toast.makeText(this, "BlUNO Activity onDestroy", Toast.LENGTH_LONG).show();
        onDestroyProcess();                                                        //onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {                                            //Four connection state
            case isConnected:
                buttonScan.setImageResource(R.drawable.ic_estado_conectado);
                estadoMonitoreo.setEnabled(true);
                break;
            case isConnecting:
                btn_eliminar.setEnabled(false);
                obtenerWearables(this);
                buttonScan.setImageResource(R.drawable.ic_estado_conectando);
                estadoMonitoreo.setEnabled(false);
                break;
            case isToScan:
                btn_eliminar.setEnabled(true);
                buttonScan.setImageResource(R.drawable.ic_estado_scan);
                estadoMonitoreo.setEnabled(false);
                break;
            case isScanning:
                buttonScan.setImageResource(R.drawable.ic_estado_is_scanning);
                estadoMonitoreo.setEnabled(false);
                break;
            case isDisconnecting:
                buttonScan.setImageResource(R.drawable.ic_estado_desconectando);
                estadoMonitoreo.setEnabled(false);
                break;
            default:
                buttonScan.setImageResource(R.drawable.ic_estado_no_conectado);
                estadoMonitoreo.setEnabled(false);
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {                            //Once connection data received, this function will be called
        // TODO Auto-generated method stub
        serialReceivedText.setText(String.format("%s%s", serialReceivedText.getText(), theString));    //append the text into the EditText
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        ((ScrollView) serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }

    public void Salir() {
        Bundle PasarBoleta = new Bundle();
        Intent siguiente = new Intent(ConectarBluno.this, MenuPrincipal.class);

        // Damos una clave = Boleta y el Objeto de tipo String = RContraseña
        PasarBoleta.putString("Boleta", BoletaRecibida);

        // Pasamos el objeto de tipo Bundle como parametro a la activity siguiente.
        siguiente.putExtras(PasarBoleta);
        startActivity(siguiente);
        finish();
    }

    // Se anula el botón que nos regresa
    @Override
    public void onBackPressed() {
    }
}