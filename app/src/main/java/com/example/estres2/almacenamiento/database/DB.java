package com.example.estres2.almacenamiento.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.estres2.almacenamiento.entidades.archivo.Archivo;
import com.example.estres2.almacenamiento.entidades.usuario.Usuario;
import com.example.estres2.almacenamiento.entidades.wearable.Wearable;
import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {
    // Versión de la base de datos
    private static final int VERSION_BD = 1;

    // Nombre de la base de datos
    private static final String NOMBRE_BD = "usermanager";

    // Nombre de la tabla Usuario
    private static final String TABLA_USUARIO = "usuarios";

    // Columnas de la tabla Usuario
    private static final String COLUMNA_USUARIO_BOLETA = "boleta";
    private static final String COLUMNA_USUARIO_NOMBRE = "nombre";
    private static final String COLUMNA_USUARIO_EDAD = "edad";
    private static final String COLUMNA_USUARIO_GENERO = "genero";
    private static final String COLUMNA_USUARIO_SEMESTRE = "semestre";
    private static final String COLUMNA_USUARIO_PASSWORD = "contraseña";
    private static final String COLUMNA_USUARIO_IMAGEN = "imagen";

    // Sentencia SQL para la creación de la tabla Usuario
    private static final String CREATE_TABLA_USUARIOS = "create table if not exists " + TABLA_USUARIO + "(" + COLUMNA_USUARIO_BOLETA + " text primary key, " + COLUMNA_USUARIO_NOMBRE + " text, "
            + COLUMNA_USUARIO_EDAD + " text, " + COLUMNA_USUARIO_GENERO + " text, " + COLUMNA_USUARIO_SEMESTRE + " text, " + COLUMNA_USUARIO_PASSWORD + " text, " + COLUMNA_USUARIO_IMAGEN + " text" + ");";

    // Nombre de la tabla Wearable
    private static final String TABLA_WEARABLE = "wearable";

    // Columnas de la tabla  Wearable
    private static final String COLUMNA_WEARABLE_ID = "id_wearable";
    private static final String COLUMNA_WEARABLE_MAC = "mac";

    // Sentencia SQL para la creación de la tabla Wearable
    private static final String CREATE_TABLA_WEARABLE = "create table if not exists " + TABLA_WEARABLE + "(" + COLUMNA_WEARABLE_ID + " text primary key, " + COLUMNA_WEARABLE_MAC + " text" + ");";

    // Nombre de la tabla Archivo
    private static final String TABLA_ARCHIVO = "archivo";

    // Columnas de la tabla  Archivo
    private static final String COLUMNA_ARCHIVO_ID = "id_archivo";
    private static final String COLUMNA_ARCHIVO_BOLETA_USUARIO = "boleta";

    // Sentencia SQL para la creación de la tabla archivo
    private static final String CREATE_TABLA_ARCHIVO = "create table if not exists " + TABLA_ARCHIVO + "(" + COLUMNA_ARCHIVO_ID + " text primary key, " + COLUMNA_ARCHIVO_BOLETA_USUARIO + " text" + ");";


    // Constructor de la clase en la que se encuentra la crear base de datos
    public DB(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    // Función para la creación de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_USUARIOS);
        db.execSQL(CREATE_TABLA_WEARABLE);
        db.execSQL(CREATE_TABLA_ARCHIVO);
    }

    // Función que se encarga de la actualización de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLA_USUARIO + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLA_WEARABLE + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + TABLA_ARCHIVO + "'");
        onCreate(db);
    }

    // Función que nos permite insertar un nuevo usuario
    public long InsertarUsuario(Usuario user) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(COLUMNA_USUARIO_BOLETA, user.getBoleta());
            values.put(COLUMNA_USUARIO_NOMBRE, user.getNombre());
            values.put(COLUMNA_USUARIO_EDAD, user.getEdad());
            values.put(COLUMNA_USUARIO_GENERO, user.getGenero());
            values.put(COLUMNA_USUARIO_SEMESTRE, user.getSemestre());
            values.put(COLUMNA_USUARIO_PASSWORD, user.getPassword());
            values.put(COLUMNA_USUARIO_IMAGEN,user.getImagen());
            long insert = db.insert(TABLA_USUARIO, null, values);
            db.close();
            return insert;
        }
        return 0;
    }

    // Función que realiza la consulta para la visualización de todos los Uusarios registrados regresa una ArrayList que contiene a todos los Usuarios
    public List<Usuario> MostrarUsuario() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Usuario> UsuariosLista = new ArrayList<>();
        UsuariosLista.clear();
        Cursor fila = db.rawQuery("select * from usuarios", null);
        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            do {
                UsuariosLista.add(
                        new Usuario(
                                fila.getString(0),
                                fila.getString(1),
                                fila.getString(2),
                                fila.getString(3),
                                fila.getString(4),
                                fila.getString(5),
                                fila.getString(6)
                        )
                );
            } while (fila.moveToNext());
        } else {
            return UsuariosLista;
        }
        return UsuariosLista;
    }

    public String RecuperarPassword(String Boleta, String Password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_USUARIO_PASSWORD, Password);
        if (db.update(TABLA_USUARIO, values, COLUMNA_USUARIO_BOLETA + "=" + Boleta, null) > 0) {
            db.close();
            return "Corregido";
        }
        return "SinBoleta";
    }

    public Usuario ObtenerDatos(String Boleta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario AuxUsuario = new Usuario("", "", "", "", "", "", "");
        Cursor fila = db.rawQuery("select *from " + TABLA_USUARIO + " where " + COLUMNA_USUARIO_BOLETA + " = " + Boleta, null);
        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            AuxUsuario = new Usuario(
                    fila.getString(0),
                    fila.getString(1),
                    fila.getString(2),
                    fila.getString(3),
                    fila.getString(4),
                    fila.getString(5),
                    fila.getString(6)
            );
            db.close();
        }
        return AuxUsuario;
    }

    // Función que nos permite modificar un usuario
    public long ActualizarUsuario(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(COLUMNA_USUARIO_NOMBRE, user.getNombre());
            values.put(COLUMNA_USUARIO_EDAD, user.getEdad());
            values.put(COLUMNA_USUARIO_GENERO, user.getGenero());
            values.put(COLUMNA_USUARIO_SEMESTRE, user.getSemestre());
            values.put(COLUMNA_USUARIO_PASSWORD, user.getPassword());
            long update = db.update(TABLA_USUARIO, values, COLUMNA_USUARIO_BOLETA + "=" + user.getBoleta(), null);
            db.close();
            return update;
        }
        return 0;
    }

    public long ActualizarImagen(Usuario user) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(COLUMNA_USUARIO_IMAGEN, user.getImagen());
            long update = db.update(TABLA_USUARIO, values, COLUMNA_USUARIO_BOLETA + "=" + user.getBoleta(), null);
            db.close();
            return update;
        }
        return 0;
    }

    // Función que nos permite borrar un usurio
    public long BorrarUsuario(String Boleta) {
        SQLiteDatabase db = this.getWritableDatabase();
        long Borrar = db.delete(TABLA_USUARIO, COLUMNA_USUARIO_BOLETA + " = " + Boleta, null);
        db.close();
        return Borrar;
    }

    // ********************** Funciones de Wearable *************************
    // Función que nos permite insertar un nuevo wearable
    public long InsertarWearable(Wearable wearable) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(COLUMNA_WEARABLE_ID, wearable.getId());
            values.put(COLUMNA_WEARABLE_MAC, wearable.getMac());
            long insert = db.insert(TABLA_WEARABLE, null, values);
            db.close();
            return insert;
        }
        return 0;
    }

    public boolean ObtenerWearable(String Mac) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor fila = db.rawQuery("select *from " + TABLA_WEARABLE + " where " + COLUMNA_WEARABLE_MAC + " = '" + Mac + "' ", null);
        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            db.close();
            return true;
        }
        return false;
    }

    public List<Wearable> MostrarWearable() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Wearable> WearableLista = new ArrayList<>();
        WearableLista.clear();
        Cursor fila = db.rawQuery("select * from wearable", null);
        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            do {
                WearableLista.add(
                        new Wearable(
                                fila.getString(0),
                                fila.getString(1)
                        )
                );
            } while (fila.moveToNext());
        } else {
            return WearableLista;
        }
        return WearableLista;
    }

    public long BorrarWearable(String wearable) {
        SQLiteDatabase db = this.getWritableDatabase();
        long Borrar = db.delete(TABLA_WEARABLE, COLUMNA_WEARABLE_ID + " = '" + wearable + "' ", null);
        db.close();
        return Borrar;
    }

    // ********************** Funciones de Archivo *************************
    // Función que nos permite insertar un nuevo archivo
    public long InsertarArchivo(Archivo archivo) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            ContentValues values = new ContentValues();
            values.put(COLUMNA_ARCHIVO_ID,archivo.getId());
            values.put(COLUMNA_ARCHIVO_BOLETA_USUARIO, archivo.getBoleta());
            long insert = db.insert(TABLA_ARCHIVO
                    , null, values);
            db.close();
            return insert;
        }
        return 0;
    }

    public List<Archivo> MostrarArchivo() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Archivo> ArchivoLista = new ArrayList<>();
        ArchivoLista.clear();
        Cursor fila = db.rawQuery("select * from archivo", null);
        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            do {
                ArchivoLista.add(
                        new Archivo(
                                fila.getString(0),
                                fila.getString(1)
                        )
                );
            } while (fila.moveToNext());
            fila.close();
        } else {
            return ArchivoLista;
        }
        return ArchivoLista;
    }

    // Función para consultar un Archivo

    public boolean ConsultarArchivo(Archivo archivo){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor fila = db.rawQuery("select " + COLUMNA_ARCHIVO_ID + " from " + TABLA_ARCHIVO + " where " + COLUMNA_ARCHIVO_ID + " = '" + archivo.getId() + "' ", null);
        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            db.close();
            return true;
        }
        db.close();
        return false;
    }

    // Función que nos permite borrar un archivo
    public long BorrarArchivo(String archivo) {
        SQLiteDatabase db = this.getWritableDatabase();
        long Borrar = db.delete(TABLA_ARCHIVO, COLUMNA_ARCHIVO_ID + " = '" + archivo + "' ", null);
        db.close();
        return Borrar;
    }

    public long EliminarArchivos(String boleta) {
        SQLiteDatabase db = this.getWritableDatabase();
        long Borrar = db.delete(TABLA_ARCHIVO, COLUMNA_ARCHIVO_BOLETA_USUARIO + " = " + boleta, null);
        db.close();
        return Borrar;
    }
    // ********************** Fin de la clase DB ************************ //
}
