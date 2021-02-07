package com.example.estres2.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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

fun eraseRegister(register: String, context: Context): Boolean {
    val db = DB(context)
    val folder = File(Environment.getExternalStorageDirectory().toString() + "/Monitoreo" + UsuarioBoleta.getObjectBoleta().boleta)
    if (folder.exists()) {
        folder.listFiles()?.iterator()?.forEach {
            if (it.name == register && db.deletedRecord(it.name)) {
                it.delete()
                Toast.makeText(context, "Se elimino el registro correctamente.", Toast.LENGTH_LONG).show()
                return true
            }
        }
    }
    Toast.makeText(context, "No existe la carpeta del usuario", Toast.LENGTH_LONG).show()
    return false
}

fun requestPermissionExternalStorage(context: Context, activity: Activity){
    // Permisos para almacenamiento externo
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    ) {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
    }
}