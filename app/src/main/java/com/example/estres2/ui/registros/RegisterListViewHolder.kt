package com.example.estres2.ui.registros

import android.view.ContextThemeWrapper
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.MainViewModel
import com.example.estres2.R
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.databinding.ItemRegistersBinding
import com.example.estres2.util.EntropyObject
import com.example.estres2.util.FileCharacteristics
import com.example.estres2.util.FileObject
import com.example.estres2.util.eraseRegister
import com.jjoe64.graphview.series.DataPoint

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
                        Toast.makeText(viewBinding.root.context, "Analizar", Toast.LENGTH_LONG).show()
                        FileObject.setNameFile(register.idRegister)
                        clearDataGraph()
                        callbackMainViewModel.updateNotificationAnalysis(true)
                        true
                    }
                    R.id.action_graficar -> {
                        Toast.makeText(viewBinding.root.context, "Gráficar", Toast.LENGTH_LONG).show()
                        FileObject.setNameFile(register.idRegister)
                        clearDataGraph()
                        callbackMainViewModel.updateNotificationGraph(true)
                        true
                    }
                    R.id.action_ambas -> {
                        Toast.makeText(viewBinding.root.context, "Graficar y Analizar", Toast.LENGTH_LONG).show()
                        FileObject.setNameFile(register.idRegister)
                        clearDataGraph()
                        callbackMainViewModel.updateNotificationAnalysisAndGraph(true)
                        true
                    }
                    R.id.action_eliminar -> {
                        if (eraseRegister(register.idRegister, viewBinding.root.context)) {
                            callbackMainViewModel.updateRegisterList(true)
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

    private fun clearDataGraph() {
        FileCharacteristics.getFc().clear()
        FileCharacteristics.getFcTime().clear()
        FileCharacteristics.getGsr().clear()
        FileCharacteristics.getGsrTime().clear()
        EntropyObject.getGraphFC().resetData(arrayOf(DataPoint(0.0,0.0)))
        EntropyObject.getGraphGSR().resetData(arrayOf(DataPoint(0.0,0.0)))
    }
}