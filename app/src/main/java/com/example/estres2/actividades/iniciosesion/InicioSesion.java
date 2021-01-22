package com.example.estres2.actividades.iniciosesion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.estres2.MostrarArchivos;
import com.example.estres2.UsuarioBoleta;
import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.MenuPrincipal;
import com.example.estres2.MostrarUsuarios;
import com.example.estres2.R;
import com.example.estres2.actividades.recuperarpassword.RecuperarPassword;
import com.example.estres2.actividades.registrar.Registro;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;

// Actividad principal
public class InicioSesion extends AppCompatActivity {
    private EditText Boleta;
    private EditText Password;
    // Crear usuarios para no tener que registrarlos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        InicializarObjetos();
        UsuariosFake();
        Boleta.setText("2015640408");
        Password.setText("Tru$tn01");
    }

    private void UsuariosFake() {
        Usuario user = new Usuario("", "", "", "", "", "", "");
        DB bd = new DB(getApplicationContext());
        user.setBoleta("2015640017");
        user.setNombre("Alfredo Armenta Espinosa");
        user.setEdad("24");
        user.setGenero("Masculino");
        user.setSemestre("12");
        user.setPassword("Politecnico12@");
        user.setImagen("");
        bd.InsertarUsuario(user);

        user.setBoleta("2015640408");
        user.setNombre("Efraín Villegas Sánchez");
        user.setEdad("24");
        user.setGenero("Masculino");
        user.setSemestre("12");
        user.setPassword("Tru$tn01");
        user.setImagen("");
        bd.InsertarUsuario(user);

        user.setBoleta("2015640000");
        user.setNombre("Fulanito Fulano");
        user.setEdad("24");
        user.setGenero("Masculino");
        user.setSemestre("12");
        user.setPassword("Politecnico12@");
        user.setImagen("");
        bd.InsertarUsuario(user);
    }
    private void InicializarObjetos() {
        Boleta = findViewById(R.id.Usuario);
        Password = findViewById(R.id.Password);
    }

    // Función que nos permite pasar a la actividad de Registro
    public void Registrar(View view) {
        Intent siguiente = new Intent(this, Registro.class);
        startActivity(siguiente);
    }

    // Función que nos permite recuperar la contraseña
    public void RecuperarPass(View view) {
        Intent siguiente = new Intent(this, RecuperarPassword.class);
        startActivity(siguiente);
    }

    // Función que nos permite pasar  la función MenuPrincipal
    public void Ingresar(View view) {
        if (VerifyCampos()) {
            // Se crea el objeto bd para utilizar los metodos de la DB y se crea con uno nuevo
            DB bd = new DB(getApplicationContext());
            // Se crea el objeto siguiente para dar inicio a la activity MenuPrincipal
            Intent siguiente = new Intent(InicioSesion.this, MenuPrincipal.class);
            UsuarioBoleta.INSTANCE.setObjectBoleta(bd.ObtenerDatos(Boleta.getText().toString()));

            if (Password.getText().toString().equals(UsuarioBoleta.INSTANCE.getObjectBoleta().getPassword())) {
                Toast.makeText(getApplicationContext(), getText(R.string.InicioSesion), Toast.LENGTH_SHORT).show();
                startActivity(siguiente);
                finish();
            } else if (Password.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getText(R.string.BoletaNoRegistrada), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getText(R.string.ErrorContraseña), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Función auxiliar que nos ayuda a visualizar los Usuarios registrados
    public void MostrarUsuarios(View view) {
        Intent siguiente = new Intent(this, MostrarUsuarios.class);
        startActivity(siguiente);
    }

    // Función auxiliar que nos ayuda a visualizar los Usuarios registrados
    public void MostrarArchivo(View view) {
        Intent siguiente = new Intent(this, MostrarArchivos.class);
        startActivity(siguiente);
    }

    private boolean VerifyCampos() {
        if (Boleta.getText().toString().isEmpty()) {
            Boleta.setError(getString(R.string.SinBoleta), null);
            return false;
        }

        if (Boleta.length() != 10) {
            Boleta.setError(getString(R.string.LongBoleta), null);
            return false;
        }

        if (Password.getText().toString().isEmpty()) {
            Password.setError(getString(R.string.SinContraseña), null);
            return false;
        }
        return true;
    }
    // ********************** Fin de la clase InicioSesión ************************ //
}