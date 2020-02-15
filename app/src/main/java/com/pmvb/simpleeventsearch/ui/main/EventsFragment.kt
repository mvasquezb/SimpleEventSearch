package com.pmvb.simpleeventsearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pmvb.simpleeventsearch.R
import com.pmvb.simpleeventsearch.data.base.PlaceEvent
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.eventResults.observe(viewLifecycleOwner, Observer {
            if (it?.isNotEmpty() == true) {
                setupEventList(it)
            } else {
                showEmptyPlaceholder()
            }
        })

        selectedPlaceLabel.text = getString(
            R.string.place_selection,
            "${viewModel.selectedRadius}km",
            "${viewModel.selectedPlace?.primaryText}, ${viewModel.selectedPlace?.secondaryText}"
        )
    }

    private fun hideEmptyPlaceholder() {
        emptyPlaceholder.visibility = GONE
    }

    private fun showEmptyPlaceholder() {
        emptyPlaceholder.visibility = VISIBLE
        eventList.visibility = GONE
    }

    private fun setupEventList(events: List<PlaceEvent>) {
        hideEmptyPlaceholder()
        val adapter = EventAdapter(requireContext(), events)
        eventList.adapter = adapter
        eventList.visibility = VISIBLE
    }

    companion object {
        val TAG = EventsFragment::class.java.simpleName

        fun newInstance() = EventsFragment()
    }
}
