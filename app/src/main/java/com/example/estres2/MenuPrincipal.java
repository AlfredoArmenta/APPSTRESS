package com.example.estres2;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;

public class MenuPrincipal extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    public TextView Nombre;
    protected TextView Boleta;
    protected Usuario DatosUsuario;
    private ImageView FotoUsuario;

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
        FotoUsuario = Hview.findViewById(R.id.MenuImagen);
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
        // Se realiza la consulta y se guarda en un onjeto usuario
        DatosUsuario = UsuarioBoleta.INSTANCE.getObjectBoleta();
        Nombre = view.findViewById(R.id.MenuNombre);
        Nombre.setText(DatosUsuario.getNombre());
        Boleta = view.findViewById(R.id.MenuBoleta);
        Boleta.setText(DatosUsuario.getBoleta());
        if (DatosUsuario.getImagen().equals("")) {
            Toast.makeText(getApplicationContext(), "No sea seleccionado una imagen", Toast.LENGTH_LONG).show();
        } else if ((reduceBitmap(getApplicationContext(), DatosUsuario.getImagen(), 512, 512) != null)) {
            Toast.makeText(getApplicationContext(), "Se cargo la imagen correctamente", Toast.LENGTH_LONG).show();
            FotoUsuario.setImageBitmap(reduceBitmap(getApplicationContext(), DatosUsuario.getImagen(), 512, 512));
        } else {
            Toast.makeText(getApplicationContext(), "La imagen no se encuentra", Toast.LENGTH_LONG).show();
        }
    }

    public void pasarBlunoConectar() {
        Intent siguiente = new Intent(MenuPrincipal.this, ConectarBluno.class);
        startActivity(siguiente);
        finish();
    }

    @SuppressLint("IntentReset")
    public void openGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DB db = new DB(getApplicationContext());
        if (resultCode == RESULT_OK) {
            DatosUsuario.setImagen(data.getDataString());
            if (db.ActualizarImagen(DatosUsuario) > 0) {
                Toast.makeText(getApplicationContext(), "Se guardo correctamente la URL de la imagen", Toast.LENGTH_LONG).show();
                FotoUsuario.setImageBitmap(reduceBitmap(getApplicationContext(), data.getDataString(), 512, 512));
            } else {
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
}