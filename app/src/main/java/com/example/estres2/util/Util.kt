package com.example.estres2.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.estres2.UsuarioBoleta
import com.example.estres2.almacenamiento.database.DB
import java.io.File
import java.io.FileNotFoundException
import kotlin.math.ceil

fun reduceBitmap(context: Context, uri: String?, maxWidth: Float, maxHeight: Float): Bitmap? {
    return try {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeStream(context.contentResolver.openInputStream(Uri.parse(uri)), null, options)
        options.inSampleSize = ceil((options.outWidth / maxWidth).toDouble()).coerceAtLeast(ceil((options.outHeight / maxHeight).toDouble())).toInt()
        options.inJustDecodeBounds = false
        BitmapFactory.decodeStream(context.contentResolver.openInputStream(Uri.parse(uri)), null, options)
    } catch (e: FileNotFoundException) {
        Toast.makeText(context, "Fichero/recurso no encontrado.", Toast.LENGTH_LONG).show()
        e.printStackTrace()
        null
    }
}

fun eraseRegister(position: Int, context: Context): Int {
    var conteo = 0
    var cuentacsv = 0
    val db = DB(context)
    val folder = File(Environment.getExternalStorageDirectory().toString() + "/Monitoreo" + UsuarioBoleta.getObjectBoleta().boleta)
    if (folder.exists()) {
        val files = folder.listFiles()!!
        for (i in files.indices) {
            if (files[i].path.endsWith(".csv")) {
                cuentacsv++
                if (i == position + conteo) {
                    if (db.deletedRecord(files[i].name)) {
                        Toast.makeText(context, "Se elimino el registro correctamente.", Toast.LENGTH_LONG).show()
                        files[i].delete()
                    }
                }
            } else {
                conteo++
            }
        }
    } else {
        Toast.makeText(context, "No existe la carpeta del usuario", Toast.LENGTH_LONG).show()
    }
    return cuentacsv
}