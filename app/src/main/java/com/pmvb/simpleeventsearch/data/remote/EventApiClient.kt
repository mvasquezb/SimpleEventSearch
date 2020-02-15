package com.pmvb.simpleeventsearch.data.remote

import com.pmvb.simpleeventsearch.data.base.PlaceEvent
import kotlinx.coroutines.flow.*

class EventApiClient : EventSearchClient {

    override suspend fun searchEvents(
        zipCode: String?,
        latLng: Pair<Double, Double>?,
        countryCode: String?,
        radius: Double?
    ): List<PlaceEvent> {
        val placeEvents = mutableListOf<List<PlaceEvent>>()
        return apis.asFlow()
            .map { it.searchEvents(zipCode, latLng, countryCode, radius) }
            .toCollection(placeEvents)
            .flatten()
    }

    companion object {
        val apis: List<EventSearchClient> by lazy {
            listOf(TicketmasterApi())
        }
    }
}