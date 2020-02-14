package com.pmvb.simpleeventsearch.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
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
        viewModel.placeResults.observe(requireActivity(), Observer {
            if (it?.isNotEmpty() == true) {
                setupPlaceResults(it)
            }
        })
    }

    private fun setupPlaceResults(placeResults: List<PlaceResult>) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchField.addTextChangedListener {
            if (it?.isNotEmpty() == true) {
                viewModel.onQuery(it.toString())
            }
        }
    }
}
