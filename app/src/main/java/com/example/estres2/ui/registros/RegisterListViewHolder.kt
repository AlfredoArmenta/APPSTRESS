package com.example.estres2.ui.registros

import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.MainViewModel
import com.example.estres2.R
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.databinding.ItemRegistersBinding
import com.example.estres2.util.FileObject
import com.example.estres2.util.eraseRegister

class RegisterListViewHolder(private val viewBinding: ItemRegistersBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    private lateinit var callbackMainViewModel: MainViewModel

    fun onBinding(recordList: UserRegister, callback: MainViewModel) {
        callbackMainViewModel = callback
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
                        println("Analizar")
                        FileObject.setNameFile(register.idRegister)
                        callbackMainViewModel.updateNotificationAnalysis(true)
                        true
                    }
                    R.id.action_graficar -> {
                        println("Gráficar")
                        FileObject.setNameFile(register.idRegister)
                        callbackMainViewModel.updateNotificationGraph(true)
                        true
                    }
                    R.id.action_ambas -> {
                        println("Gráficar y Analizar")
                        FileObject.setNameFile(register.idRegister)
                        callbackMainViewModel.updateNotificationAnalysisAndGraph(true)
                        true
                    }
                    R.id.action_eliminar -> {
                        if (eraseRegister(register.idRegister, viewBinding.root.context)) {
                            callbackMainViewModel.updateRegisterList(true)
                        } else {
                            println("No se elimino el registro")
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
