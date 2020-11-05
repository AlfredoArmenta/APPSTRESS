package com.example.estres2.Bluetooth;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.estres2.MenuPrincipal;
import com.example.estres2.R;

public class ConectarBluno extends BlunoLibrary {
    private ImageButton buttonScan;
    private EditText serialSendText;
    private TextView serialReceivedText;

    /**************************/
    public String BoletaRecibida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluno);

        // Se inicializa BoletaRecibida
        BoletaRecibida = "";

        // Se genera un objeto Bundle para poder recibir los parametros entre actividades
        Bundle BoletaR = getIntent().getExtras();

        //  Se pregunta si BoletaR es distinta de null lo que quiere decir que se recibio sin ningún problema
        if (BoletaR != null) {
            // Se obtiene la boleta
            BoletaRecibida = BoletaR.getString("Boleta");
            Toast.makeText(getApplicationContext(),"Boleta recibida" + BoletaRecibida, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"No se pudo recuperar el usuario", Toast.LENGTH_SHORT).show();
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show();
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }else{
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show();
        }
        onCreateProcess();														//onCreate Process by BlunoLibrary


        serialBegin(115200);													//set the Uart Baudrate on BLE chip to 115200

        serialReceivedText= findViewById(R.id.serialReveicedText);	//initial the EditText of the received data
        serialSendText= findViewById(R.id.serialSendText);			//initial the EditText of the sending data

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

                serialSend(serialSendText.getText().toString());				//send the data to the BLUNO
            }
        });

        buttonScan = findViewById(R.id.buttonScanBlunoConected);					//initial the button for scanning the BLE device
        buttonScan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
            }
        });
    }

    protected void onResume(){
        super.onResume();
        Toast.makeText(this,"BlUNO Activity onResume",Toast.LENGTH_LONG).show();
        System.out.println("BlUNOActivity onResume");
        onResumeProcess();														//onResume Process by BlunoLibrary
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"BlUNO Activity onPause",Toast.LENGTH_LONG).show();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }

    protected void onStop() {
        super.onStop();
        Toast.makeText(this,"BlUNO Activity onStop",Toast.LENGTH_LONG).show();
        onStopProcess();														//onStop Process by BlunoLibrary
    }

    @Override
    protected void onDestroy() {
        //serialSend("F"); //Finish Terminar la comunicación
        super.onDestroy();
        Toast.makeText(this,"BlUNO Activity onDestroy",Toast.LENGTH_LONG).show();
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

    @Override
    public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
        switch (theConnectionState) {											//Four connection state
            case isConnected:
                buttonScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_estado_conectado));
                break;
            case isConnecting:
                buttonScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_estado_conectando));
                break;
            case isToScan:
                buttonScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_estado_scan));
                break;
            case isScanning:
                buttonScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_estado_is_scanning));
                break;
            case isDisconnecting:
                buttonScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_estado_desconectando));
                break;
            default: buttonScan.setImageDrawable(getResources().getDrawable(R.drawable.ic_estado_no_conectado));
                break;
        }
    }

    @Override
    public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
        // TODO Auto-generated method stub
        serialReceivedText.setText(String.format("%s%s", serialReceivedText.getText(), theString));	//append the text into the EditText
        //The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
        ((ScrollView)serialReceivedText.getParent()).fullScroll(View.FOCUS_DOWN);
    }

    public void Salir() {
        Bundle PasarBoleta = new Bundle();

        Intent siguiente = new Intent(ConectarBluno.this, MenuPrincipal.class);

        // Damos una clave = Boleta y el Objeto de tipo String = RContraseña
        PasarBoleta.putString("Boleta",BoletaRecibida);

        // Pasamos el objeto de tipo Bundle como parametro a la activity siguiente.
        siguiente.putExtras(PasarBoleta);
        startActivity(siguiente);
        finish();
    }

    // Se anula el botón que nos regresa
    @Override public void onBackPressed() {}

}

