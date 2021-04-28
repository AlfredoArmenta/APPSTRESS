package com.example.estres2.actividades.bluetooth.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.actividades.bluetooth.viewmodel.BlunoViewModel
import com.example.estres2.almacenamiento.entidades.wearable.Wearable
import com.example.estres2.databinding.ItemWearableBinding

class AdapterWearable(private var ListaWearable: List<Wearable>, private val callback: BlunoViewModel) : RecyclerView.Adapter<WearableViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WearableViewHolder = WearableViewHolder(
            ItemWearableBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: WearableViewHolder, position: Int) {
        holder.onBinding(ListaWearable[position], callback)
    }

    override fun getItemCount(): Int = ListaWearable.size
}
