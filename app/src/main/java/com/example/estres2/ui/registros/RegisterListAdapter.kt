package com.example.estres2.ui.registros

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.databinding.ListaRegistrosBinding
import com.example.estres2.ui.viewmodel.MenuViewModel

class RegisterListAdapter(private val registerList: List<ListaRegistro>, private val callback: MenuViewModel): RecyclerView.Adapter<ListaRegistrosViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaRegistrosViewHolder = ListaRegistrosViewHolder(
            ListaRegistrosBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: ListaRegistrosViewHolder, position: Int) {
        holder.onBinding(registerList[position], callback)
    }

    override fun getItemCount(): Int = registerList.size
}