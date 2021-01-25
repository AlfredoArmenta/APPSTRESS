package com.example.estres2.ui.registros

import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.R
import com.example.estres2.databinding.ListaRegistrosBinding

class ListaRegistrosViewHolder(private val viewBinding: ListaRegistrosBinding) :
        RecyclerView.ViewHolder(
                viewBinding.root) {
    fun onBinding(recordList: ListaRegistro) {
        viewBinding.apply {
            RVTextView.text = recordList.nombreRegistro
            RVEliminar.setImageDrawable(ContextCompat.getDrawable(root.context,
                    recordList.imagenEliminar))
            if (RVTextView.text != "Carpeta Vacia") {
                RVEliminar.setOnClickListener {
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
                        Toast.makeText(viewBinding.root.context, "Analizar", Toast.LENGTH_LONG)
                                .show()
                        true
                    }
                    R.id.action_graficar -> {
                        Toast.makeText(viewBinding.root.context, "Gráficar", Toast.LENGTH_LONG)
                                .show()
                        true
                    }
                    R.id.action_ambas -> {
                        Toast.makeText(viewBinding.root.context,
                                "Graficar y Analizar",
                                Toast.LENGTH_LONG).show()
                        true
                    }
                    R.id.action_eliminar -> {
                        Toast.makeText(viewBinding.root.context, "Eliminar", Toast.LENGTH_LONG)
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