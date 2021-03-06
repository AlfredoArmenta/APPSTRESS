package com.example.estres2.ui.registros

import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.R
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.databinding.ItemRegistersBinding
import com.example.estres2.ui.viewmodel.MenuViewModel
import com.example.estres2.util.eraseRegister

class RegisterListViewHolder(private val viewBinding: ItemRegistersBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    private lateinit var callbackErase: MenuViewModel

    fun onBinding(recordList: UserRegister, callback: MenuViewModel) {
        callbackErase = callback
        viewBinding.apply {
            RVTextView.text = recordList.idRegister
            RVEliminar.setImageDrawable(ContextCompat.getDrawable(root.context, recordList.imageRegister))
            if (RVTextView.text != "Carpeta Vacia") {
                RVEliminar.setOnClickListener {
                    showPopupMenu(recordList)
                }
            }
        }
    }

    private fun showPopupMenu(register: UserRegister) {
        val wrapper = ContextThemeWrapper(viewBinding.root.context, R.style.MyPopupMenu)
        val popupMenu = PopupMenu(wrapper, viewBinding.root)
        popupMenu.apply {
            inflate(R.menu.menu_registers)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_analizar -> {
                        Toast.makeText(viewBinding.root.context, "Analizar", Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.action_graficar -> {
                        Toast.makeText(viewBinding.root.context, "Gráficar", Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.action_ambas -> {
                        Toast.makeText(viewBinding.root.context, "Graficar y Analizar", Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.action_eliminar -> {
                        if (eraseRegister(register.idRegister, viewBinding.root.context)) {
                            callbackErase.updateRegisterList(true)
                        } else {
                            Toast.makeText(viewBinding.root.context, "No se elimino el registro", Toast.LENGTH_LONG).show()
                        }
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