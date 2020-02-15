package com.pmvb.simpleeventsearch.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pmvb.simpleeventsearch.R
import com.pmvb.simpleeventsearch.data.base.PlaceEvent
import com.pmvb.simpleeventsearch.util.format

class EventAdapter(
    private val context: Context,
    private val events: List<PlaceEvent>,
    private val clickHandler: (PlaceEvent) -> Unit = {}
) : RecyclerView.Adapter<EventVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventVH {
        val view = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)
        return EventVH(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: EventVH, position: Int) {
        val event = events[position]
        holder.bind(event)
        holder.itemView.setOnClickListener { clickHandler(event) }
    }

}

class EventVH(root: View) : RecyclerView.ViewHolder(root) {
    val eventName = itemView.findViewById<TextView>(R.id.eventName)
    val eventLocation = itemView.findViewById<TextView>(R.id.eventLocation)
    val eventDate = itemView.findViewById<TextView>(R.id.eventDate)

    fun bind(event: PlaceEvent) {
        eventName.text = event.name
        eventLocation.text = event.location
        eventDate.text = event.startDate?.format("yyyy-MM-dd HH:mm") ?: "Date TBA"
    }
}
