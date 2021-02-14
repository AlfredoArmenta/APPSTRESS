package com.example.estres2.actividades.bluetooth.adapters

import android.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.R
import com.example.estres2.actividades.bluetooth.viewmodel.BlunoViewModel
import com.example.estres2.almacenamiento.basededatos.DB
import com.example.estres2.almacenamiento.entidades.wearable.Wearable
import com.example.estres2.databinding.ItemWearableBinding

class WearableViewHolder(private val viewBinding: ItemWearableBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    private lateinit var callbackEraser: BlunoViewModel
    fun onBinding(recordList: Wearable, callback: BlunoViewModel) {
        callbackEraser = callback
        viewBinding.apply {
            mid.text = recordList.id
            mmac.text = recordList.mac
            mBorrar.setImageDrawable(ContextCompat.getDrawable(root.context, R.drawable.ic_erase_wearable))
            if (recordList.id != "Wearable no registrado") {
                mBorrar.setOnClickListener {
                    showPopupMenu(recordList)
                }
            }
        }
    }

    private fun showPopupMenu(register: Wearable) {
        val wrapper = ContextThemeWrapper(viewBinding.root.context, R.style.MyPopupMenu)
        val popupMenu = PopupMenu(wrapper, viewBinding.root)
        popupMenu.apply {
            inflate(R.menu.menu_delete_wearable)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_eliminar_wearable -> {
                        AlertDialog.Builder(ContextThemeWrapper(viewBinding.root.context, R.style.AlertDialogCustom))
                                .setTitle("Eliminar Wearable")
                                .setIcon(R.drawable.ic_erase_wearable)
                                .setMessage("¿Deseas eliminar el Wearable?")
                                .setPositiveButton("Aceptar") { _, _ ->
                                    val bd = DB(viewBinding.root.context)
                                    if (bd.deleteWearable(register.id)) {
                                        Toast.makeText(viewBinding.root.context, "Se Elimino correctamente el Wearable", Toast.LENGTH_LONG).show()
                                        callbackEraser.updateWearablesList(true)
                                    } else {
                                        Toast.makeText(viewBinding.root.context, "No se Borro ni madres", Toast.LENGTH_LONG).show()
                                    }
                                }
                                .setNegativeButton("Cancelar") { _, _ -> }
                                .show()
                        true
                    }
                    else -> false
                }
            }
            gravity = Gravity.END
            show()
        }
    }
}