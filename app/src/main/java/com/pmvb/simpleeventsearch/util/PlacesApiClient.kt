package com.pmvb.simpleeventsearch.util

import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.pmvb.simpleeventsearch.MyApplication

class PlacesApiClient {
    companion object {
        val token by lazy {
            AutocompleteSessionToken.newInstance()
        }
    }

    fun query(query: String): Task<FindAutocompletePredictionsResponse> {
        val request = buildRequest(query)
        return MyApplication.placesClient.findAutocompletePredictions(request)
    }

    private fun buildRequest(query: String): FindAutocompletePredictionsRequest {
        return FindAutocompletePredictionsRequest.builder()
            .setSessionToken(token)
            .setQuery(query)
            .build()
    }
}
