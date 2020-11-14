package com.example.estres2.actividades.recuperarpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.R;
import com.example.estres2.actividades.iniciosesion.InicioSesion;

public class RecuperarPassword extends AppCompatActivity {

    // Inicializamos los objetos
    private EditText Boleta;
    private EditText Contraseña;
    private TextView Numero;
    private TextView CaracterEspecial;
    private TextView Mayuscula;
    private TextView Minuscula;
    private TextView Longitud;

    private int ColorVerde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);
        IniciarObjetos();

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

    // Función con la cual inicializamos los objetos que se utilizarán los registros
    public void IniciarObjetos() {
        Boleta = (EditText) findViewById(R.id.CBoleta);
        Contraseña = (EditText) findViewById(R.id.CContraseña);
        Numero = (TextView) findViewById(R.id.CTNumero);
        CaracterEspecial = (TextView) findViewById(R.id.CTCaracterEspecial);
        Mayuscula = (TextView) findViewById(R.id.CTMayuscula);
        Minuscula = (TextView) findViewById(R.id.CTMinuscula);
        Longitud = (TextView) findViewById(R.id.CTLongitud);
        ColorVerde = Color.GREEN;
    }

    // Función que que valida la boleta y actualiza la contraseña
    public void Aceptar(View view) {
        DB bd = new DB(getApplicationContext());
        String Rboleta, Rcontraseña;
        Rboleta = Boleta.getText().toString();
        Rcontraseña = Contraseña.getText().toString();

        if (!Rboleta.equals("") && !Rcontraseña.equals("")) {
            if (VerifyCampos()) {
                if (bd.RecuperarContraseña(Rboleta, Rcontraseña).equals("Corregido")) {
                    Toast.makeText(getApplicationContext(),
                            getText(R.string.ActualizoCorrectamente), Toast.LENGTH_SHORT).show();
                    Intent siguiente = new Intent(this, InicioSesion.class);
                    startActivity(siguiente);
                    finish();
                } else {
                    Boleta.setError(getString(R.string.BoletaNoEncontrada));
                    Toast.makeText(getApplicationContext(),
                            getText(R.string.BoletaNoRegistrada), Toast.LENGTH_SHORT).show();
                }
            }
        } else if (!Rboleta.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.SinContraseña, Toast.LENGTH_LONG).show();
        } else if (!Rcontraseña.equals("")) {
            Toast.makeText(getApplicationContext(), R.string.SinBoleta, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.SinBoleta, Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), R.string.SinContraseña, Toast.LENGTH_LONG).show();
        }
    }

    // Función que nos proporciona si se esta cumpliendo con el llenado de los paramestros para el registro
    private boolean VerifyCampos() {
        // Preguntamos si esta vacio el campo de Boleta
        if (Boleta.getText().toString().isEmpty()) {
            Boleta.setError(getString(R.string.SinBoleta), null);
            Toast.makeText(getApplicationContext(), getString(R.string.SinBoleta), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si la longitud es de 10
        if (Boleta.length() != 10) {
            Boleta.setError(getString(R.string.LongBoleta), null);
            Toast.makeText(getApplicationContext(), getString(R.string.LongBoleta), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si esta vacio el campo de Contraseña esta vacio
        if (Contraseña.getText().toString().isEmpty()) {
            Contraseña.setError(getString(R.string.SinContraseña), null);
            Toast.makeText(getApplicationContext(), getString(R.string.SinContraseña), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Preguntamos si todas las restricciones para la contraseña estan en verde si es así la contraseña es valida
        return Longitud.getCurrentTextColor() == ColorVerde && CaracterEspecial.getCurrentTextColor() == ColorVerde &&
                Numero.getCurrentTextColor() == ColorVerde && Minuscula.getCurrentTextColor() == ColorVerde &&
                Mayuscula.getCurrentTextColor() == ColorVerde;
    }

    // Función que valida los parametros con las restrigcciones de la contraseña
    private void password() {
        String Password = Contraseña.getText().toString().trim();

        // Preguntamos si la longitud de la contraseña esta comprendida en un rango de 8 a 15 caracteres
        if (!Password.matches(".{8,15}")) {
            Longitud.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_too_short_password), null);
        } else {
            Longitud.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene algún caracter especial
        if (!Password.matches(".*[!@#$%^*+=¿?_-].*")) {
            CaracterEspecial.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_special_caracter), null);
        } else {
            CaracterEspecial.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene algún digito
        if (!Password.matches(".*\\d.*")) {
            Numero.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_number), null);
        } else {
            Numero.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene alguna minúscula
        if (!Password.matches(".*[a-z].*")) {
            Minuscula.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_lowercase_caracter), null);
        } else {
            Minuscula.setTextColor(Color.GREEN);
        }

        // Preguntamos si se contiene alguna mayúscula
        if (!Password.matches(".*[A-Z].*")) {
            Mayuscula.setTextColor(Color.RED);
            Contraseña.setError(getString(R.string.error_not_find_uppercase_caracter), null);
        } else {
            Mayuscula.setTextColor(Color.GREEN);
        }
    }
    // ********************** Fin de la clase RecuperarPassword ************************ //
}
