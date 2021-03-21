package com.example.estres2.ui.inico

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.almacenamiento.entidades.cardview.CardView
import com.example.estres2.databinding.ItemCardViewBinding

class CardViewAdapter(private val listCardView: List<CardView>) : RecyclerView.Adapter<CardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder = CardViewHolder(
            ItemCardViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
            )
    )

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.onBinding(listCardView[position])
    }

    override fun getItemCount(): Int = listCardView.size
}