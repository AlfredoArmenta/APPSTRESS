package com.example.estres2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.example.estres2.actividades.bluetooth.ConectarBluno;
import com.example.estres2.actividades.iniciosesion.InicioSesion;
import com.example.estres2.almacenamiento.database.DB;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MenuPrincipal extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    public TextView Nombre;
    protected TextView Boleta;
    protected String BoletaRecibida;
    protected Usuario DatosUsuario;
    protected DB Consultar;
    private ImageView foto_gallery;

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
                pasarBlunoConectar();
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
        foto_gallery = Hview.findViewById(R.id.MenuImagen);
        pasarBoleta(Hview);
        // Permisos para almacenamiento externo
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
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

    public void pasarBoleta(View view) {
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
            if (!DatosUsuario.getBoleta().equals("")) {
                Nombre = (TextView) view.findViewById(R.id.MenuNombre);
                Nombre.setText(DatosUsuario.getNombre());
                Boleta = (TextView) view.findViewById(R.id.MenuBoleta);
                Boleta.setText(BoletaRecibida);
                if (!DatosUsuario.getImagen().equals(""))
                    foto_gallery.setImageBitmap(reduceBitmap(getApplicationContext(), DatosUsuario.getImagen(), 1024, 1024));
            } else {
                Toast.makeText(getApplicationContext(), "No se pudo recuperar el usuario", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Ocurrio un error al recuperar la boleta", Toast.LENGTH_SHORT).show();
        }
    }

    public void pasarBlunoConectar() {
        Bundle PasarBoleta = new Bundle();
        Intent siguiente = new Intent(MenuPrincipal.this, ConectarBluno.class);

        // Damos una clave = Boleta y el Objeto de tipo String = RContraseña
        PasarBoleta.putString("Boleta", BoletaRecibida);

        // Pasamos el objeto de tipo Bundle como parametro a la activity siguiente.
        siguiente.putExtras(PasarBoleta);
        startActivity(siguiente);
        finish();
    }

    @SuppressLint("IntentReset")
    public void openGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent,"Seleccione la aplicación"),10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DB db = new DB(getApplicationContext());
        if (resultCode == RESULT_OK){
            DatosUsuario.setImagen(data.getDataString());
            if(db.ActualizarImagen(DatosUsuario) > 0){
                Toast.makeText(getApplicationContext(), "Se guardo correctamente la URL de la imagen", Toast.LENGTH_LONG).show();
                foto_gallery.setImageBitmap(reduceBitmap(getApplicationContext(), data.getDataString(), 512, 512));
            }else {
                Toast.makeText(getApplicationContext(), "Ocurrio un error al guardar la URL de la imagen", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static Bitmap reduceBitmap(Context contexto, String uri,
                                      float maxAncho, float maxAlto) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
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