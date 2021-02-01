package com.example.estres2.ui.registros

import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.R
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.databinding.ListaRegistrosBinding
import com.example.estres2.ui.viewmodel.MenuViewModel
import com.example.estres2.util.eraseRegister

class RegisterListViewHolder(private val viewBinding: ListaRegistrosBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    private lateinit var callbackErase: MenuViewModel

    fun onBinding(recordList: UserRegister, callback: MenuViewModel) {
        callbackErase = callback
        viewBinding.apply {
            RVTextView.text = recordList.idRegister
            RVEliminar.setImageDrawable(ContextCompat.getDrawable(root.context, recordList.imageRegister))
            if (RVTextView.text != "Carpeta Vacia") {
                RVEliminar.setOnClickListener {
                    Toast.makeText(viewBinding.root.context, adapterPosition.toString(), Toast.LENGTH_LONG).show()
                    showPopupMenu()
                }
            }
        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(viewBinding.root.context, viewBinding.root)
        popupMenu.apply {
            inflate(R.menu.menu_registros)
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
                        Toast.makeText(viewBinding.root.context, "Eliminar", Toast.LENGTH_LONG).show()
                        if (eraseRegister(adapterPosition, viewBinding.root.context) <= 1) {
                            viewBinding.apply {
                                RVEliminar.setImageResource(R.drawable.ic_sin_registro)
                                RVEliminar.setOnClickListener(null)
                                RVTextView.text = "Carpeta Vacia"
                            }
                        } else {
                            callbackErase.updateRegisterList(true)
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