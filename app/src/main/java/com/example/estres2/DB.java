package com.example.estres2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {
    // Versión de la base de datos
    private static final int VERSION_BD = 1;

    //Nombre de la base de datos
    private static final String NOMBRE_BD = "usermanager";

    // Nombre de la tabla Usuario
    private static final String TABLA_USUARIO = "usuarios";

    // Columnas de la tabla
    private static final String COLUMNA_USUARIO_BOLETA = "boleta";
    private static final String COLUMNA_USUARIO_NOMBRE = "nombre";
    private static final String COLUMNA_USUARIO_EDAD = "edad";
    private static final String COLUMNA_USUARIO_GENERO = "genero";
    private static final String COLUMNA_USUARIO_SEMESTRE = "semestre";
    private static final String COLUMNA_USUARIO_CONTRASEÑA = "contraseña";

    // Sentencia SQL para la creación de una tabla
    private static final String CREATE_TABLA_USUARIOS = "create table if not exists " + TABLA_USUARIO + "(" + COLUMNA_USUARIO_BOLETA + " text primary key, " + COLUMNA_USUARIO_NOMBRE + " text, "
            + COLUMNA_USUARIO_EDAD + " text, " + COLUMNA_USUARIO_GENERO + " text, " + COLUMNA_USUARIO_SEMESTRE + " text, " + COLUMNA_USUARIO_CONTRASEÑA + " text" + ");";

    // Constructor de la clase en la que se encuentra la crear base de datos
    public DB(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    // Función para la creación de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLA_USUARIOS);
    }

    // Función que se encarga de la actualización de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLA_USUARIO + "'");
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
            values.put(COLUMNA_USUARIO_CONTRASEÑA, user.getContraseña());
            long insert = db.insert(TABLA_USUARIO, null, values);
            db.close();
            return insert;
        }
        return 0;
    }

    // Función que confima la existencia de la boleta ingresada y regresa como parametro la contraseña asociada a la cuenta
    public String IniciarSesion(String Boleta) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Contraseña = "";
        Cursor fila = db.rawQuery("select " + COLUMNA_USUARIO_CONTRASEÑA + " from " + TABLA_USUARIO + " where " + COLUMNA_USUARIO_BOLETA + " = " + Boleta, null);

        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            Contraseña = fila.getString(0);
            db.close();
            return Contraseña;
        } else {
            return Contraseña;
        }
    }

    // Función que realiza la consulta para la visualización de todos los Uusarios registrados regresa una ArrayList que contiene a todos los Usuarios
    public Cursor Mostrar() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from usuarios", null);
    }

    public String RecuperarContraseña(String Boleta, String Contraseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMNA_USUARIO_CONTRASEÑA, Contraseña);

        if (db.update(TABLA_USUARIO, values, COLUMNA_USUARIO_BOLETA + "=" + Boleta, null) > 0) {
            db.close();
            return "Corregido";
        }
        return "SinBoleta";
    }

    public Usuario ObtenerDatos(String Boleta) {
        SQLiteDatabase db = this.getReadableDatabase();
        Usuario AuxUsuario = new Usuario("", "", "", "", "", "");
        Cursor fila = db.rawQuery("select *from " + TABLA_USUARIO + " where " + COLUMNA_USUARIO_BOLETA + " = " + Boleta, null);

        if (fila != null && fila.getCount() != 0) {
            fila.moveToFirst();
            AuxUsuario = new Usuario(
                    fila.getString(0),
                    fila.getString(1),
                    fila.getString(2),
                    fila.getString(3),
                    fila.getString(4),
                    fila.getString(5)
            );
            db.close();
            return AuxUsuario;
        } else {
            return AuxUsuario;
        }
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
            values.put(COLUMNA_USUARIO_CONTRASEÑA, user.getContraseña());
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
    // ********************** Fin de la clase DB ************************ //
}
