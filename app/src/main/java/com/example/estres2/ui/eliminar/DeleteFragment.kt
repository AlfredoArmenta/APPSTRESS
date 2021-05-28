package com.example.estres2.ui.eliminar

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.estres2.MainActivity
import com.example.estres2.R
import com.example.estres2.util.UserObject.getObjectBoleta
import com.example.estres2.actividades.iniciosesion.Login
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.usuario.User
import com.example.estres2.databinding.FragmentDeleteUserBinding
import java.io.File

class DeleteFragment : Fragment() {
    private var _binding: FragmentDeleteUserBinding? = null
    private val binding get() = _binding!!
    private lateinit var mContext: Context
    private lateinit var user: User
    private lateinit var bd: DB
    private lateinit var notification: NotificationCompat.Builder

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDeleteUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = binding.root.context
        initializeObjects()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initializeObjects() {
        user = getObjectBoleta()
        bd = DB(mContext)
        binding.apply {
            CEliminar.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, R.style.AlertDialogCustom))
                        .setTitle("Eliminar Cuenta")
                        .setIcon(R.drawable.ic_remove_user)
                        .setMessage("Si aceptas se eliminarán los datos de la cuenta y los registros permanentemente.")
                        .setPositiveButton("Aceptar") { _, _ ->
                            deleteAccount()
                        }
                        .setNegativeButton("Cancelar") { _, _ ->
                            println("Se conservó la cuenta")
                        }
                        .show()
            }
        }
    }

    private fun deleteAccount() {
        val folder = File(Environment.getExternalStorageDirectory().toString() + "/Monitoreo" + user.boleta)
        if (folder.exists()) {
            deleteRecursive(folder)
            if (!folder.exists()) {
                Toast.makeText(mContext, "Carpeta borrada correctamente.", Toast.LENGTH_LONG).show()
                if (bd.deletedRecordsAndDirectory(user.boleta)) {
                    Toast.makeText(mContext, "Archivos borrados de la base de datos correctamente.", Toast.LENGTH_LONG).show()
                    deleteUser()
                    setNotification()
                    back()
                } else {
                    Toast.makeText(mContext, "Los archivos no fueron borrados de la base de datos correctamente.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(mContext, "La carpeta no fue borrada correctamente.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(mContext, "La carpeta no existe.", Toast.LENGTH_LONG).show()
            deleteUser()
            back()
        }
    }

    private fun deleteUser() {
        if (bd.deleteUser(user.boleta)) {
            Toast.makeText(mContext, "Se eliminó el usuario con boleta: " + user.boleta + " y con nombre: " + user.nombre + ".", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(mContext, "No se pudo eliminar el usuario con boleta: " + user.boleta + " y con nombre: " + user.nombre + ".", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) fileOrDirectory.listFiles()?.forEach { child ->
            deleteRecursive(child)
        }
        fileOrDirectory.delete()
    }

    private fun setNotification() {
        notification = NotificationCompat.Builder(requireContext(), MainActivity.CHANNEL_0_ID).apply {
            setContentTitle("Cuenta")
            setContentText("Se ha eliminado la cuenta")
            setSubText("Información Personal")
            setSmallIcon(R.drawable.ic_login)
            color = ContextCompat.getColor(requireContext(), R.color.colorPrimary)
            priority = NotificationCompat.PRIORITY_LOW
        }
        NotificationManagerCompat.from(requireContext()).apply {
            notify(MainActivity.NOTIFICATION_0, notification.build())
        }
    }

    private fun back() {
        startActivity(Intent(context, Login::class.java))
        activity?.finish()
    }
}
