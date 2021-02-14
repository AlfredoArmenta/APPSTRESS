package com.example.estres2.ui.registros

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.almacenamiento.entidades.registros.UserRegister
import com.example.estres2.databinding.ItemRegistersBinding
import com.example.estres2.ui.viewmodel.MenuViewModel

class RegisterListAdapter(private val registerList: List<UserRegister>, private val callback: MenuViewModel) : RecyclerView.Adapter<RegisterListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterListViewHolder = RegisterListViewHolder(
            ItemRegistersBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: RegisterListViewHolder, position: Int) {
        holder.onBinding(registerList[position], callback)
    }

    override fun getItemCount(): Int = registerList.size
}