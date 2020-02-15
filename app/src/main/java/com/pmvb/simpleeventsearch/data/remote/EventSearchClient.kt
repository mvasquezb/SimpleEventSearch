package com.pmvb.simpleeventsearch.data.remote

import com.pmvb.simpleeventsearch.data.base.PlaceEvent

interface EventSearchClient {
    suspend fun searchEvents(
        zipCode: String? = null,
        latLng: Pair<Double, Double>? = null,
        countryCode: String? = null,
        radius: Double? = null
    ): List<PlaceEvent>
}
