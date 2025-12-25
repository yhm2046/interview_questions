package com.hackerrank.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView

class DealtCardsAdapter(
    context: Context,
    private val cards: List<Int>,
    private val selectedIndex: Int,
    private val onCardClick: (Int) -> Unit
) : ArrayAdapter<Int>(context, 0, cards) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.selected_card_item, parent, false)

        val image = view.findViewById<ImageView>(R.id.grid_item_image)
        val drawableId = cards[position]

        image.setImageResource(drawableId)

        image.setTag(R.string.drawable_identifer, drawableId)

        if (position == selectedIndex) {
            image.setColorFilter(
                R.color.black,
                android.graphics.PorterDuff.Mode.SRC_ATOP
            )
        } else {
            image.colorFilter = null
        }

        image.setOnClickListener {
            onCardClick(position)
        }

        return view
    }
}