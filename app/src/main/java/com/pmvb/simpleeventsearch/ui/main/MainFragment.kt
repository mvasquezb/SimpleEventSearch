package com.pmvb.simpleeventsearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmvb.simpleeventsearch.R
import com.pmvb.simpleeventsearch.data.base.PlaceResult
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.loading.observe(requireActivity(), Observer {
            if (it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        })

        viewModel.placeResults.observe(requireActivity(), Observer {
            if (it?.isNotEmpty() == true) {
                setupPlaceResults(it)
            } else {
                showEmptyPlaceholder()
            }
        })

        viewModel.placeError.observe(requireActivity(), Observer {
            if (it != null) {
                it.printStackTrace()
                Log.e("mainfragment", "placeError: $it")
                showEmptyPlaceholder(it.message)
            }
        })
    }

    private fun showEmptyPlaceholder(message: String? = null) {
        if (message == null) {
            emptyPlaceholder.text = requireContext().getString(R.string.no_results_placeholder)
        } else {
            emptyPlaceholder.text = message
        }
        searchResults.visibility = View.GONE
        emptyPlaceholder.visibility = View.VISIBLE
    }

    private fun setupPlaceResults(placeResults: List<PlaceResult>) {
        Log.e("mainfragment", "size: ${placeResults.size}. places: $placeResults")
        val adapter = PlacesAdapter(requireContext(), placeResults) {
            selectPlace(it)
        }
        searchResults.layoutManager = LinearLayoutManager(context)
        searchResults.itemAnimator = DefaultItemAnimator()
        searchResults.adapter = adapter
        searchResults.visibility = View.VISIBLE
    }

    private fun selectPlace(place: PlaceResult) {
        Log.e("mainfragment", "place clicked: $place")
        selectedText.text = "Selected: ${place.primaryText}"
        viewModel.selectedPlace = place
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchField.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                viewModel.onQuery(it.toString())
                emptyPlaceholder.visibility = View.GONE
                selectedText.visibility = View.GONE
            }
        }
    }
}
