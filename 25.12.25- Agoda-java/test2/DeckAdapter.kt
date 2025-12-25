package com.hackerrank.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.hackerrank.android.databinding.DeckItemBinding

class DeckAdapter(
    private val cards: List<Int>,
    private val onClick: (Int) -> Unit
) : RecyclerView.Adapter<DeckAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.deck_item, parent, false)
        
        val layoutParams = itemView.layoutParams
        layoutParams.width = (parent.measuredWidth / 3.5).toInt()
        itemView.layoutParams = layoutParams
        
        return VH(itemView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val drawableId = cards[position]
        val itemImage = holder.itemView.findViewById<ImageView>(R.id.item_image)
        
        itemImage.setImageResource(drawableId)
        itemImage.setTag(R.string.drawable_identifer, drawableId)

        itemImage.setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int = cards.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView)
}