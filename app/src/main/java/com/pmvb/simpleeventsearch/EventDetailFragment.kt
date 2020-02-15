package com.pmvb.simpleeventsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pmvb.simpleeventsearch.data.base.PlaceEvent
import com.pmvb.simpleeventsearch.ui.main.MainViewModel
import com.pmvb.simpleeventsearch.util.format
import com.pmvb.simpleeventsearch.util.openLink
import kotlinx.android.synthetic.main.fragment_event_detail.*

class EventDetailFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.selectedEvent.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setupEventView(it)
                viewModel.transitionToEventDetailComplete()
            }
        })
    }

    private fun setupEventView(event: PlaceEvent) {
        eventName.text = event.name
        eventLocation.text = event.location
        eventStartDate.text = event.startDate?.format("yyyy-MM-dd HH:mm") ?: "TBA"
        if (event.endDate != null) {
            endDateContainer.visibility = View.GONE
        } else {
            endDateContainer.visibility = View.VISIBLE
            eventEndDate.text = event.startDate?.format("yyyy-MM-dd HH:mm") ?: "TBA"
        }
        eventSource.text = event.source
        if (event.url != null) {
            eventLink.visibility = View.VISIBLE
            eventLink.setOnClickListener { context?.openLink(event.url) }
        } else {
            eventLink.visibility = View.GONE
        }
    }

    companion object {
        val TAG = EventDetailFragment::class.java.simpleName
        fun newInstance() = EventDetailFragment()
    }
}
