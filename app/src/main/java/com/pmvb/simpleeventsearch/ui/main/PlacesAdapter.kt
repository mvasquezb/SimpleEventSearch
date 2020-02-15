package com.pmvb.simpleeventsearch.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pmvb.simpleeventsearch.R
import com.pmvb.simpleeventsearch.data.base.PlaceResult

class PlacesAdapter(
    private val context: Context,
    private val places: List<PlaceResult>,
    private val clickHandler: (PlaceResult) -> Unit = {}
) : RecyclerView.Adapter<PlaceVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceVH {
        val view = LayoutInflater.from(context).inflate(R.layout.place_result_item, parent, false)
        return PlaceVH(view)
    }

    override fun getItemCount(): Int = places.size

    override fun onBindViewHolder(holder: PlaceVH, position: Int) {
        val place = places[position]
        holder.bind(place)
        holder.itemView.setOnClickListener { clickHandler(place) }
    }
}

class PlaceVH(root: View) : RecyclerView.ViewHolder(root) {
    val placeTitle = itemView.findViewById<TextView>(R.id.placeTitle)
    val placeSubitle = itemView.findViewById<TextView>(R.id.placeSubtitle)

    lateinit var place: PlaceResult

    fun bind(place: PlaceResult) {
        placeTitle.text = place.primaryText
        placeSubitle.text = place.secondaryText
    }
}
