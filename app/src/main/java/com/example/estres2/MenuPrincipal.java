package com.example.estres2;

import android.content.Intent;
import android.os.Bundle;
import com.example.estres2.Bluetooth.ConectarBluno;
import com.example.estres2.ui.registros.ListaRegistroAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public TextView Nombre;
    public TextView Boleta;
    public String BoletaRecibida;
    public Usuario DatosUsuario;
    public DB Consultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Has presentado un episodio de estrés", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // Se crea el objeto PasarUsuario que nos permite enviar objetos de un activity a otra
                Bundle PasarBoleta = new Bundle();

                Intent siguiente = new Intent(MenuPrincipal.this, ConectarBluno.class);
                // Damos una clave = Boleta y el Objeto de tipo String = RContraseña
                PasarBoleta.putString("Boleta",BoletaRecibida);

                // Pasamos el objeto de tipo Bundle como parametro a la activity siguiente.
                siguiente.putExtras(PasarBoleta);
                startActivity(siguiente);
                finish();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_graficas, R.id.nav_registro,
                R.id.nav_configurar_cuenta, R.id.nav_eliminar)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Se crea un componente View para poder mostrar el contenido
        View Hview = navigationView.getHeaderView(0);

        // Se inicializa BoletaRecibida
        BoletaRecibida = "";

        // Se genera un objeto Bundle para poder recibir los parametros entre actividades
        Bundle BoletaR = getIntent().getExtras();

        //  Se pregunta si BoletaR es distinta de null lo que quiere decir que se recibio sin ningún problema
        if (BoletaR != null) {
            // Se obtiene la boleta
            BoletaRecibida = BoletaR.getString("Boleta");

            // Se crea un objeto DB para poder consultas todos los demás paramétros del usuario
            Consultar = new DB(getApplicationContext());

            // Se realiza la consulta y se guarda en un onjeto usuario
            DatosUsuario = Consultar.ObtenerDatos(BoletaRecibida);
            if ( DatosUsuario.getBoleta() != "") {
                Nombre = (TextView) Hview.findViewById(R.id.MenuNombre);
                Nombre.setText(DatosUsuario.getNombre());
                Boleta = (TextView) Hview.findViewById(R.id.MenuBoleta);
                Boleta.setText(BoletaRecibida);

            }else {
                Toast.makeText(getApplicationContext(),"No se pudo recuperar el usuario", Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(getApplicationContext(),"Ocurrio un error al recuperar la boleta", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void Salir(MenuItem item) {
        Intent siguiente = new Intent(MenuPrincipal.this, InicioSesion.class);
        startActivity(siguiente);
        finish();
    }

    public Usuario MandarUsuario() {
        return this.DatosUsuario;
    }
}