package com.example.estres2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Registro extends AppCompatActivity {

    // Creamos los objetos en la parte logica para la manipulación del registro
    private EditText Boleta;
    private EditText Nombre;
    private EditText Edad;
    private RadioButton Masculino;
    private RadioButton Femenino;
    private Spinner Semestre;
    private Spinner UA;
    private EditText Contraseña;

    private TextView Requerimientos;
    private TextView Numero;
    private TextView CaracterEspecial;
    private TextView Mayuscula;
    private TextView Minuscula;
    private TextView Longitud;

    // Variable del color verde
    private int ColorVerde;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        IniciarObjetos();
    }

    // Función con la cual inicializamos los objetos que se utilizarán los registros
    private void IniciarObjetos(){

        Boleta = (EditText) findViewById(R.id.RBoleta);
        Nombre = (EditText) findViewById(R.id.RNombre);
        Edad = (EditText) findViewById(R.id.REdad);
        Masculino = (RadioButton) findViewById(R.id.RMasculino);
        Femenino = (RadioButton) findViewById(R.id.RFemenino);
        Semestre = (Spinner) findViewById(R.id.RSemestre);
        UA = (Spinner) findViewById(R.id.RMateria);
        Contraseña = (EditText) findViewById(R.id.RContraseña);

        Masculino.setChecked(true);

        // String que nos ayudan a llenar a los spinners que sirven para la selección del semestre y la materia
        String [] semestre = {"Selecciona tu semestre actual", "1", "2", "3", "4", "5","6", "7", "8", "9", "10","11", "12", "13", "14", "15"};
        ArrayAdapter<String> AdapterSemestre = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, semestre);
        Semestre.setAdapter(AdapterSemestre);

        String [] UnidadAprendizaje = {"Selecciona una materia", "Lineas de Transmisión y Antenas", "Teoría de la Información", "Teoría de las Comunicaciones", "Variable Compleja",
                "Protocolos de Internet", "Comunicaciones Digitales", "Sistemas Distribuidos", "Metodología", "Sistemas Celulares", "Multimedia","Señales y Sistemas", "Probabilidad",
                "Programación de Dispositivos Móviles", "PT1", "PT2"};
        ArrayAdapter<String> AdapterUnidad = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, UnidadAprendizaje);
        UA.setAdapter(AdapterUnidad);

        // Elementos de texto que nos proporcionan información sobre los parametros de la contraseña
        Requerimientos = (TextView) findViewById(R.id.RRContraseña);
        Numero = (TextView) findViewById(R.id.RTNumero);
        CaracterEspecial = (TextView) findViewById(R.id.RTCaracterEspecial);
        Mayuscula = (TextView) findViewById(R.id.RTMayuscula);
        Minuscula = (TextView) findViewById(R.id.RTMinuscula);
        Longitud = (TextView) findViewById(R.id.RTLongitud);

        // El valor númerico del color verde es = -16711936
        ColorVerde = -16711936;

        // Esta función nos permite estar checacodo si se ha modificado el texto del EditText Contraseña
        Contraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * This method is to initialize objects to be used
     */

    // Función que nos regresa ala actividad de Inicio de Sesión
    public void Cancelar(View view) {
        Intent siguiente = new Intent(this, InicioSesion.class);
        startActivity(siguiente);
        finish();

    }

    // Registro que nos permite realizar el registro de un nuevo Usuario
    public void Registrar(View view) {
        RegistrarSQL();

    }

    // Función que nos permite visualizar a todos los usuarios registrados
    public void Mostrar (View view) {
        Intent siguiente = new Intent(this, Mostrar.class);
        startActivity(siguiente);
        finish();
    }


    //En está función se obtienen los valores de entrada, se validan y se registra al usuario
    private void RegistrarSQL() {

        DB bd = new DB(getApplicationContext());
        Usuario user = new Usuario();

        user.setBoleta(Boleta.getText().toString());
        user.setNombre(Nombre.getText().toString());
        user.setEdad(Edad.getText().toString());

        if (Masculino.isChecked() == true) {
            user.setGenero("Masculino");
        } else {
            user.setGenero("Femenino");
        }
        user.setSemestre(Semestre.getSelectedItem().toString());
        user.setUnidadA(UA.getSelectedItem().toString());
        user.setContraseña(Contraseña.getText().toString());

        if ( VerifyCampos() == true) {

            if (bd.InsertarUsuario(user) > 0) {
                Toast.makeText(getApplicationContext(),
                        getText(R.string.InicioCorrecto), Toast.LENGTH_SHORT).show();
                Intent siguiente = new Intent(this, InicioSesion.class);
                startActivity(siguiente);
                finish();

            } else {
                Toast.makeText(getApplicationContext(),
                        getText(R.string.BoletaRegistrada), Toast.LENGTH_SHORT).show();

            }

        }

    }

    // Función que nos proporciona si se esta cumpliendo con el llenado de los paramestros para el registro
    private  boolean VerifyCampos() {

        // Preguntamos si esta vacio el campo de Boleta
        if ( Boleta.getText().toString().isEmpty() ) {
            Boleta.setError(getString(R.string.SinBoleta));
            return false;
        }

        // Preguntamos si la longitud es de 10
        if ( Boleta.length() != 10 ){
            Boleta.setError(getString(R.string.LongBoleta));
            return false;
        }

        // Preguntamos si esta vacio el campo de nombre
        if ( Nombre.getText().toString().isEmpty() ) {
            Nombre.setError(getString(R.string.SinNombre));
            return false;
        }

        // Preguntamos si esta vacio el campo de Edad
        if ( Edad.getText().toString().isEmpty() ) {
            Edad.setError(getString(R.string.SinEdad));
            return false;
        }

        // Resteingimos la Edad en un rango de 10 a 99 años
        if ( Integer.parseInt(String.valueOf(Edad.getText())) < 20 || Integer.parseInt(String.valueOf(Edad.getText())) > 25){
            Edad.setError(getString(R.string.RangoEdad));
            return false;
        }

        // Preguntamos si no se selecciona un semestre
        if ( Semestre.getSelectedItem().toString() == "Selecciona tu semestre actual" ) {
            Toast.makeText(getApplicationContext(),
                    getText(R.string.SinSemestre), Toast.LENGTH_SHORT).show();
            return false;
        }

        /*  Preguntamos si no se selecciona una unidad de aprendizaje
        if ( UA.getSelectedItem().toString() == "Selecciona una materia" ) {
            Toast.makeText(getApplicationContext(),
                    getText(R.string.SinMateria), Toast.LENGTH_SHORT).show();
            return false;
        }*/

        // Preguntamos si esta vacio el campo de Contraseña esta vacio
        if ( Contraseña.getText().toString().isEmpty() ) {
            Contraseña.setError(getString(R.string.SinContraseña));
            return false;
        }

        // Preguntamos si todas las restricciones para la contraseña estan en verde si es así la contraseña es valida
        if ( !(Longitud.getCurrentTextColor() == ColorVerde && CaracterEspecial.getCurrentTextColor() == ColorVerde &&
                Numero.getCurrentTextColor() == ColorVerde && Minuscula.getCurrentTextColor() == ColorVerde &&
                Mayuscula.getCurrentTextColor() == ColorVerde) ) {
            return false;
        }

        return true;
    }

    // Función que valida los parametros con las restrigcciones de la contraseña
    private void password() {

        String Password = Contraseña.getText().toString().trim();

        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
        if (!Password.matches(".{8,15}")) {
            Longitud.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_too_short_password));
        } else {
            Longitud.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene algún caracter especial
        if (!Password.matches(".*[!@#$%^*+=¿?_-].*")) {
            CaracterEspecial.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_special_caracter));;
        } else {
            CaracterEspecial.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene algún digito
        if (!Password.matches(".*\\d.*")) {
            Numero.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_number));
        } else {
            Numero.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene alguna minúscula
        if (!Password.matches(".*[a-z].*")) {
            Minuscula.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_lowercase_caracter));
        } else {
            Minuscula.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene alguna mayúscula
        if (!Password.matches(".*[A-Z].*")) {
            Mayuscula.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_uppercase_caracter));
        } else {
            Mayuscula.setTextColor(Color.GREEN);
        }

    }

    // ********************** Fin de la clase Registro ************************ //
}

