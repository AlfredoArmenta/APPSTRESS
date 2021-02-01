package com.example.estres2.almacenamiento.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.estres2.almacenamiento.entidades.archivo.Archivo
import com.example.estres2.almacenamiento.entidades.usuario.Usuario
import com.example.estres2.almacenamiento.entidades.wearable.Wearable
import kotlinx.coroutines.withContext
import java.util.ArrayList

class DB(context: Context): SQLiteOpenHelper(context, NOMBRE_BD, null, VERSION_BD) {
    private lateinit var row: Cursor
    private var auxUser: Usuario? = null
    private var userList: MutableList<Usuario> = ArrayList()
    private var wearableList: MutableList<Wearable> = ArrayList()
    private var fileList: MutableList<Archivo> = ArrayList()
    private var insert: Int? = null
    private var update: Int? = null
    private var erase: Int? = null

    // Función para la creación de la base de datos
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLA_USUARIOS)
        db.execSQL(CREATE_TABLA_WEARABLE)
        db.execSQL(CREATE_TABLA_ARCHIVO)
    }

    // Función que se encarga de la actualización de la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.run {
            execSQL("DROP TABLE IF EXISTS '$TABLA_USUARIO'")
            execSQL("DROP TABLE IF EXISTS '$TABLA_WEARABLE'")
            execSQL("DROP TABLE IF EXISTS '$TABLA_ARCHIVO'")
            onCreate(this)
        }
    }

    // ********************** Funciones de Usuario *************************
    // Función que nos permite insertar un nuevo Usuario
    fun insertUser(user: Usuario): Boolean {
        val values = ContentValues()
        values.put(COLUMNA_USUARIO_BOLETA, user.boleta)
        values.put(COLUMNA_USUARIO_NOMBRE, user.nombre)
        values.put(COLUMNA_USUARIO_EDAD, user.edad)
        values.put(COLUMNA_USUARIO_GENERO, user.genero)
        values.put(COLUMNA_USUARIO_SEMESTRE, user.semestre)
        values.put(COLUMNA_USUARIO_PASSWORD, user.password)
        values.put(COLUMNA_USUARIO_IMAGEN, user.imagen)
        insert = this.writableDatabase.insert(TABLA_USUARIO, null, values).toInt()
        this.writableDatabase.close()
        return insert!! >= 0
    }

    fun showUser(): List<Usuario> {
        row = this.readableDatabase.rawQuery("select * from usuarios", null)
        when {
            row.count != 0 -> {
                row.moveToFirst()
                do {
                    userList.add(
                            Usuario(
                                    row.getString(0),
                                    row.getString(1),
                                    row.getString(2),
                                    row.getString(3),
                                    row.getString(4),
                                    row.getString(5),
                                    row.getString(6)
                            )
                    )
                } while (row.moveToNext())
                this.readableDatabase.close()
                return userList
            }
            else -> {
                this.readableDatabase.close()
                return userList
            }
        }
    }

    fun recoverPassword(Boleta: String, Password: String?): Boolean {
        val values = ContentValues()
        values.put(COLUMNA_USUARIO_PASSWORD, Password)
        return when {
            this.writableDatabase.update(TABLA_USUARIO, values, "$COLUMNA_USUARIO_BOLETA=$Boleta", null) > 0 -> {
                this.writableDatabase.close()
                true
            }
            else -> {
                this.writableDatabase.close()
                false
            }
        }
    }

    fun getUser(Boleta: String): Usuario? {
        row = this.readableDatabase.rawQuery("select *from $TABLA_USUARIO where $COLUMNA_USUARIO_BOLETA = $Boleta", null)
        when {
            row.count != 0 -> {
                row.moveToFirst()
                auxUser = Usuario(
                        row.getString(0),
                        row.getString(1),
                        row.getString(2),
                        row.getString(3),
                        row.getString(4),
                        row.getString(5),
                        row.getString(6)
                )
            }
        }
        this.readableDatabase.close()
        return auxUser
    }

    fun checkUser(Boleta: String): Boolean{
        row = this.writableDatabase.rawQuery("select *from $TABLA_USUARIO where $COLUMNA_USUARIO_BOLETA = $Boleta", null)
        return when {
            row.count != 0 -> {
                this.readableDatabase.close()
                true
            } else -> {
                this.readableDatabase.close()
                false
            }
        }
    }

    fun updateUser(user: Usuario): Boolean {
        val values = ContentValues()
        values.put(COLUMNA_USUARIO_NOMBRE, user.nombre)
        values.put(COLUMNA_USUARIO_EDAD, user.edad)
        values.put(COLUMNA_USUARIO_GENERO, user.genero)
        values.put(COLUMNA_USUARIO_SEMESTRE, user.semestre)
        values.put(COLUMNA_USUARIO_PASSWORD, user.password)
        update = this.writableDatabase.update(TABLA_USUARIO, values, COLUMNA_USUARIO_BOLETA + "=" + user.boleta, null)
        this.writableDatabase.close()
        return update!! >= 0
    }

    fun deleteUser(Boleta: String): Boolean {
        erase = this.writableDatabase.delete(TABLA_USUARIO, "$COLUMNA_USUARIO_BOLETA = $Boleta", null)
        this.writableDatabase.close()
        return erase!! >= 0
    }

    // ********************** Funciones de Imagen *************************
    // Función que nos permite insertar una nueva Imagen
    fun updateImage(user: Usuario): Boolean {
        val values = ContentValues()
        values.put(COLUMNA_USUARIO_IMAGEN, user.imagen)
        update = this.writableDatabase.update(TABLA_USUARIO, values, """$COLUMNA_USUARIO_BOLETA=${user.boleta}""", null)
        this.writableDatabase.close()
        return update!! >= 0
    }

    // ********************** Funciones de Wearable *************************
    // Función que nos permite insertar un nuevo wearable
    fun insertWearable(wearable: Wearable): Boolean {
        val values = ContentValues()
        values.put(COLUMNA_WEARABLE_ID, wearable.id)
        values.put(COLUMNA_WEARABLE_MAC, wearable.mac)
        insert = this.writableDatabase.insert(TABLA_WEARABLE, null, values).toInt()
        this.writableDatabase.close()
        return insert!! >= 0
    }

    fun getWearable(Mac: String): Boolean {
        row = this.readableDatabase.rawQuery("select *from $TABLA_WEARABLE where $COLUMNA_WEARABLE_MAC = '$Mac' ", null)
        return when {
            row.count != 0 -> {
                row.moveToFirst()
                this.readableDatabase.close()
                true
            }
            else -> {
                this.readableDatabase.close()
                false
            }
        }
    }

    fun showWearable(): List<Wearable> {
        row = this.readableDatabase.rawQuery("select * from wearable", null)
        when {
            row.count != 0 -> {
                row.moveToFirst()
                do {
                    wearableList.add(
                            Wearable(
                                    row.getString(0),
                                    row.getString(1)
                            )
                    )
                } while (row.moveToNext())
            }
        }
        this.readableDatabase.close()
        return wearableList
    }

    fun deleteWearable(wearable: String): Boolean {
        erase = this.writableDatabase.delete(TABLA_WEARABLE, "$COLUMNA_WEARABLE_ID = '$wearable' ", null)
        this.writableDatabase.close()
        return erase!! >= 0
    }

    // ********************** Funciones de Archivo *************************
    // Función que nos permite insertar un nuevo archivo
    fun insertRecord(Record: Archivo): Boolean {
        val values = ContentValues()
        values.put(COLUMNA_ARCHIVO_ID, Record.id)
        values.put(COLUMNA_ARCHIVO_BOLETA_USUARIO, Record.boleta)
        insert = this.writableDatabase.insert(TABLA_ARCHIVO, null, values).toInt()
        this.writableDatabase.close()
        return insert!! >= 0
    }

    fun showRecord(): List<Archivo> {
        row = this.readableDatabase.rawQuery("select * from archivo", null)
        when {
            row.count != 0 -> {
                row.moveToFirst()
                do {
                    fileList.add(
                            Archivo(
                                    row.getString(0),
                                    row.getString(1)
                            )
                    )
                } while (row.moveToNext())
            }
        }
        row.close()
        return fileList
    }

    fun getRecord(Record: Archivo): Boolean {
        row = this.readableDatabase.rawQuery("select $COLUMNA_ARCHIVO_ID from $TABLA_ARCHIVO where $COLUMNA_ARCHIVO_ID = '${Record.id}' ", null)
        return when {
            row.count != 0 -> {
                row.moveToFirst()
                this.readableDatabase.close()
                true
            }
            else -> {
                this.readableDatabase.close()
                false
            }
        }
    }

    fun deletedRecord(Record: String): Boolean {
        erase = writableDatabase.delete(TABLA_ARCHIVO, "$COLUMNA_ARCHIVO_ID = '$Record' ", null)
        this.writableDatabase.close()
        return erase!! >= 0
    }

    fun deletedRecordsAndDirectory(Boleta: String): Boolean {
        erase = writableDatabase.delete(TABLA_ARCHIVO, "$COLUMNA_ARCHIVO_BOLETA_USUARIO = $Boleta", null)
        this.writableDatabase.close()
        return erase!! >= 0
    } // ********************** Fin de la clase DB ************************ //

    companion object {
        // Versión de la base de datos
        private const val VERSION_BD = 1

        // Nombre de la base de datos
        private const val NOMBRE_BD = "usermanager"

        // Nombre de la tabla Usuario
        private const val TABLA_USUARIO = "usuarios"

        // Columnas de la tabla Usuario
        private const val COLUMNA_USUARIO_BOLETA = "boleta"
        private const val COLUMNA_USUARIO_NOMBRE = "nombre"
        private const val COLUMNA_USUARIO_EDAD = "edad"
        private const val COLUMNA_USUARIO_GENERO = "genero"
        private const val COLUMNA_USUARIO_SEMESTRE = "semestre"
        private const val COLUMNA_USUARIO_PASSWORD = "contraseña"
        private const val COLUMNA_USUARIO_IMAGEN = "imagen"

        // Sentencia SQL para la creación de la tabla Usuario
        private const val CREATE_TABLA_USUARIOS =
                ("""create table if not exists $TABLA_USUARIO($COLUMNA_USUARIO_BOLETA text primary key, $COLUMNA_USUARIO_NOMBRE text, $COLUMNA_USUARIO_EDAD text, $COLUMNA_USUARIO_GENERO text, $COLUMNA_USUARIO_SEMESTRE text, $COLUMNA_USUARIO_PASSWORD text, $COLUMNA_USUARIO_IMAGEN text);""")

        // Nombre de la tabla Wearable
        private const val TABLA_WEARABLE = "wearable"

        // Columnas de la tabla  Wearable
        private const val COLUMNA_WEARABLE_ID = "id_wearable"
        private const val COLUMNA_WEARABLE_MAC = "mac"

        // Sentencia SQL para la creación de la tabla Wearable
        private const val CREATE_TABLA_WEARABLE =
                "create table if not exists $TABLA_WEARABLE($COLUMNA_WEARABLE_ID text primary key, $COLUMNA_WEARABLE_MAC text);"

        // Nombre de la tabla Archivo
        private const val TABLA_ARCHIVO = "archivo"

        // Columnas de la tabla  Archivo
        private const val COLUMNA_ARCHIVO_ID = "id_archivo"
        private const val COLUMNA_ARCHIVO_BOLETA_USUARIO = "boleta"

        // Sentencia SQL para la creación de la tabla archivo
        private const val CREATE_TABLA_ARCHIVO =
                "create table if not exists $TABLA_ARCHIVO($COLUMNA_ARCHIVO_ID text primary key, $COLUMNA_ARCHIVO_BOLETA_USUARIO text);"
    }
}