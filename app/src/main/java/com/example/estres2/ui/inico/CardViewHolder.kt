package com.example.estres2.ui.inico

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.estres2.almacenamiento.entidades.cardview.CardView
import com.example.estres2.databinding.ItemCardViewBinding

class CardViewHolder(private val viewBinding: ItemCardViewBinding) : RecyclerView.ViewHolder(viewBinding.root) {
    fun onBinding(listCard: CardView) {
        viewBinding.apply {
            imageCard.setImageDrawable(ContextCompat.getDrawable(viewBinding.root.context, listCard.imageCardView))
            titleTopicCard.text = listCard.titleCardView
            subtitleTopicCard.text = listCard.subtitleCardView
        }
    }
}
