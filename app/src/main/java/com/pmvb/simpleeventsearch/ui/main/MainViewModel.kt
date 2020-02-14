package com.pmvb.simpleeventsearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.pmvb.simpleeventsearch.data.base.PlaceResult
import com.pmvb.simpleeventsearch.util.PlacesApiClient

class MainViewModel : ViewModel() {
    private val _placeResults = MutableLiveData<List<PlaceResult>>()
    val placeResults: LiveData<List<PlaceResult>> = _placeResults
    private val _loading = MutableLiveData<Boolean>().apply { value = false }
    val loading: LiveData<Boolean> = _loading
    private val _placeError = MutableLiveData<Exception>()
    val placeError: LiveData<Exception> = _placeError

    private val placesClient = PlacesApiClient()
    var selectedPlace: PlaceResult? = null

    private fun searchPlace(query: String) {
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

    companion object {
        const val QUERY_SPAN = 500L
    }
}
