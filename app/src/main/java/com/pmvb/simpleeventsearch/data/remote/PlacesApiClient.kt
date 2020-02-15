package com.pmvb.simpleeventsearch.data.remote

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.pmvb.simpleeventsearch.MyApplication

class PlacesApiClient {
    companion object {
        val token by lazy {
            AutocompleteSessionToken.newInstance()
        }
        val placeDetailsList = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS_COMPONENTS
        )
        private val placesClient by lazy {
            MyApplication.placesClient
        }
    }

    fun query(query: String): Task<FindAutocompletePredictionsResponse> {
        val request = buildPredictionRequest(query)
        return placesClient.findAutocompletePredictions(request)
    }

    private fun buildPredictionRequest(query: String): FindAutocompletePredictionsRequest {
        return FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()
    }

    fun getPlaceDetails(placeId: String): Task<FetchPlaceResponse> {
        val request = buildPlaceDetailRequest(placeId)
        return placesClient.fetchPlace(request)
    }

    private fun buildPlaceDetailRequest(placeId: String): FetchPlaceRequest {
        return FetchPlaceRequest.newInstance(placeId,
            placeDetailsList
        )
    }
}
