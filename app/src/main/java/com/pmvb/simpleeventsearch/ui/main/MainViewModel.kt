package com.pmvb.simpleeventsearch.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.pmvb.simpleeventsearch.data.base.PlaceEvent
import com.pmvb.simpleeventsearch.data.base.PlaceResult
import com.pmvb.simpleeventsearch.util.PlacesApiClient

class MainViewModel : ViewModel() {
    private val _placeResults = MutableLiveData<List<PlaceResult>>()
    val placeResults: LiveData<List<PlaceResult>> = _placeResults
    private val _loading = MutableLiveData<Boolean>().apply { value = false }
    val loading: LiveData<Boolean> = _loading
    private val _placeError = MutableLiveData<Exception>()
    val placeError: LiveData<Exception> = _placeError
    private val _eventsReady = MutableLiveData<Boolean>().apply { value = false }
    val eventsReady: LiveData<Boolean> = _eventsReady
    private val _eventResults = MutableLiveData<List<PlaceEvent>>()
    val eventResults: LiveData<List<PlaceEvent>> = _eventResults

    private val placesClient = PlacesApiClient()
    var selectedPlace: PlaceResult? = null
    var selectedRadius: Int = 0

    private fun searchPlace(query: String) {
        _placeError.value = null
        _loading.value = true
        placesClient.query(query)
            .addOnCompleteListener { _loading.value = false }
            .addOnSuccessListener {
                _placeResults.value = buildPlaceResults(it.autocompletePredictions)
            }
            .addOnFailureListener {
                _placeError.value = it
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
        if (query.isNotEmpty()) {
            searchPlace(query)
        }
    }

    fun onSearch() {
        val (place, radius) = selectedPlace to selectedRadius
        if (place != null && radius > 0) {
            searchEvents(place, radius)
        }
    }

    private fun searchEvents(place: PlaceResult, radius: Int) {
        _loading.value = true
        _eventsReady.value = true
        _eventResults.value = listOf()
        _loading.value = false
    }

    companion object {
        const val QUERY_SPAN = 500L
    }
}
