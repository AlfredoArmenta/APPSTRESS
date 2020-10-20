package com.example.estres2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.estres2.ui.cuenta.CuentaFragment;


// Actividad principal
public class InicioSesion extends AppCompatActivity {

    private EditText Boleta;
    private EditText Contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        InicializarObjetos();
        Boleta.setText("2015640017");
        Contraseña.setText("Politecnico1@");
    }

    private void InicializarObjetos() {
        Boleta = (EditText) findViewById(R.id.Usuario);
        Contraseña = (EditText) findViewById(R.id.Password);
    }

    // Función que nos permite pasar a la actividad de Registro
    public void Registrar(View view){

        Intent siguiente = new Intent(this, Registro.class);
        startActivity(siguiente);
    }

    // Función que nos permite recuperar la contraseña
    public void RecuperarPass(View view){

        Intent siguiente = new Intent(this, RecuperarPassword.class);
        startActivity(siguiente);
    }

    // Función que nos permite pasar  la función MenuPrincipal
    public void Ingresar(View view){

        if ( VerifyCampos() ) {

            // Se crea el objeto bd para utilizar los metodos de la DB y se crea con uno nuevo
            DB bd = new DB(getApplicationContext());

            // Se crea el objeto AuxUsuario para obtener los parametros de los usuarios y enviarlos a la siguiente actividad
            String RContraeña;

            // Se crea el objeto PasarUsuario que nos permite enviar objetos de un activity a otra
            Bundle PasarBoleta = new Bundle();

            // Se crea el objeto siguiente para dar inicio a la activity MenuPrincipal
            Intent siguiente = new Intent(InicioSesion.this, MenuPrincipal.class);

            String IBoleta = Boleta.getText().toString();
            String IContraseña = Contraseña.getText().toString();

            RContraeña = bd.IniciarSesion(IBoleta);

            if ( IContraseña.equals(RContraeña) ) {
                Toast.makeText(getApplicationContext(),
                        getText(R.string.InicioSesion), Toast.LENGTH_SHORT).show();

                // Damos una clave = Boleta y el Objeto de tipo String = RContraseña
                PasarBoleta.putString("Boleta",IBoleta);

                // Pasamos el objeto de tipo Bundle como parametro a la activity siguiente.
                siguiente.putExtras(PasarBoleta);

                startActivity(siguiente);
                finish();

            } else if ( RContraeña.equals("") ) {
                Toast.makeText(getApplicationContext(),
                        getText(R.string.BoletaNoRegistrada), Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getApplicationContext(),
                        getText(R.string.ErrorContraseña), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Función auxiliar que nos ayuda a visualizar los Usuarios registrados
    public void Mostrar (View view) {
        Intent siguiente = new Intent(this, Mostrar.class);
        startActivity(siguiente);
    }

    private  boolean VerifyCampos() {

        if ( Boleta.getText().toString().isEmpty() ) {
            Boleta.setError(getString(R.string.SinBoleta));
            return false;
        }

        if ( Boleta.length() != 10 ){
            Boleta.setError(getString(R.string.LongBoleta));
            return false;
        }

        if ( Contraseña.getText().toString().isEmpty() ) {
            Contraseña.setError(getString(R.string.SinContraseña));
            return false;
        }

        return true;

    }

    // ********************** Fin de la clase InicioSesión ************************ //
}