package com.pmvb.simpleeventsearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.pmvb.simpleeventsearch.data.base.PlaceResult
import com.pmvb.simpleeventsearch.util.PlacesApiClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel : ViewModel() {
    private val queryChannel = ConflatedBroadcastChannel<String>()

    private val _placeResults = MutableLiveData<List<PlaceResult>>()
    val placeResults: LiveData<List<PlaceResult>> = _placeResults

    private val placesClient = PlacesApiClient()

    init {
        queryChannel
            .asFlow()
            .debounce(QUERY_SPAN)
            .onEach { searchPlace(it) }
            .launchIn(viewModelScope)
    }

    private fun searchPlace(query: String) {
        placesClient.query(query).addOnSuccessListener {
            if (it.autocompletePredictions.isNotEmpty()) {
                _placeResults.value = buildPlaceResults(it.autocompletePredictions)
            }
        }
    }

    private fun buildPlaceResults(predictions: List<AutocompletePrediction>): List<PlaceResult> {
        return predictions.map {
            PlaceResult(
                placeId = it.placeId,
                primaryText = it.getPrimaryText(null).toString(),
                secondaryText = it.getSecondaryText(null).toString()
            )
        }
    }

    fun onQuery(query: String) {
        queryChannel.offer(query)
    }

    companion object {
        const val QUERY_SPAN = 500L
    }
}
