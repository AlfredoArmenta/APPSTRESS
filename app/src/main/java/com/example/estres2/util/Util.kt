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
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.estres2.almacenamiento.basededatos.DB
import java.io.File
import java.io.FileNotFoundException
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.min

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
    val folder = File(Environment.getExternalStorageDirectory().toString() + "/Monitoreo" + UserObject.getObjectBoleta().boleta)
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

fun requestPermissionExternalStorage(context: Context, activity: Activity) {
    // Permisos para almacenamiento externo
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
    ) {
        requestPermissions(activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
    }
}

fun requestPermissionBluetooth(context: Context, activity: Activity) {
    // Permisos para bluetooth
    val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(context, "Se requiere permiso para obtener datos de ubicación BLE", Toast.LENGTH_SHORT).show()
        } else {
            requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    } else {
        Toast.makeText(context, "Permisos de ubicación ya otorgados", Toast.LENGTH_SHORT).show()
    }
}

fun SampEn(y: DoubleArray, M: Int, r: Double): Double {
    val n = y.size
    val lastRun = IntArray(n)
    val run = IntArray(n)

    var j: Int
    var nj: Int

    val A = DoubleArray(M)
    val B = DoubleArray(M)
    val p = DoubleArray(M)
    val e = DoubleArray(M)

    for (i in 0 until n.minus(1)) {
        nj = if (i == 0) {
            n.minus(2)
        } else {
            n.minus(i).minus(2)
        }
        val y1 = y[i]

        for (jj in 0 until nj + 1) {
            j = if (i == 0) {
                jj.plus(1)
            } else {
                jj.plus(i + 1)
            }

            if (abs((y[j] - y1)) < r) {
                run[jj] = lastRun[jj].plus(1)
                val m1 = min(M, run[jj])

                for (m in 0 until m1) {
                    A[m] = A[m].plus(1)

                    if (j < (n - 1)) {
                        B[m] = B[m].plus(1)
                    }
                }
            } else {
                run[jj] = 0
            }
        }
        for (l in 0 until nj + 1) {
            lastRun[l] = run[l]
        }
    }

    val N = n * (n.minus(1)) / 2

    B[2] = B[1]
    B[1] = B[0]
    B[0] = N.toDouble()

    for (x in 0 until M) {
        p[x] = A[x] / B[x]
        e[x] = (-1) * ln(p[x])
    }
    return e[2]
}