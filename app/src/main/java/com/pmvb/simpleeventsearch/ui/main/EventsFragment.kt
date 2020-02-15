package com.pmvb.simpleeventsearch.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pmvb.simpleeventsearch.R

class EventsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    companion object {
        val TAG = EventsFragment::class.java.simpleName

        fun newInstance() = EventsFragment()
    }
}
