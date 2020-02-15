package com.pmvb.simpleeventsearch.data.remote

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.pmvb.simpleeventsearch.BuildConfig
import com.pmvb.simpleeventsearch.data.base.PlaceEvent
import com.pmvb.simpleeventsearch.util.addQueryIf
import com.pmvb.simpleeventsearch.util.toDate
import org.json.JSONObject
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TicketmasterApi : EventSearchClient {
    companion object {
        const val apiDomain = "https://app.ticketmaster.com"
        const val eventDiscoveryUrl = "/discovery/v2/events.json"

        const val FIELD_API_KEY = "apikey"
        const val FIELD_POSTAL_CODE = "postalCode"
        const val FIELD_LAT_LNG = "latlong"
        const val FIELD_RADIUS = "radius"
        const val FIELD_UNIT = "unit"
        const val FIELD_UNIT_VALUE = "km"
        const val FIELD_SIZE = "size"
        const val FIELD_SIZE_VALUE = 10
        const val FIELD_COUNTRY_CODE = "countryCode"

        const val API_RESPONSE_CONTAINER = "_embedded"
        const val API_RESPONSE_EVENTS = "events"
        const val API_RESPONSE_EVENT_NAME = "name"
        const val API_RESPONSE_EVENT_URL = "url"
        const val API_RESPONSE_EVENT_DATES = "dates"
        const val API_RESPONSE_EVENT_DATE_START = "start"
        const val API_RESPONSE_EVENT_DATE_START_DATETIME = "dateTime"
        const val API_RESPONSE_PAGE_INFO = "page"
        const val API_RESPONSE_COUNT = "totalElements"
        const val API_RESPONSE_VENUES = "venues"
        const val API_RESPONSE_VENUE_NAME = "name"
    }

    override suspend fun searchEvents(
        zipCode: String?,
        latLng: Pair<Double, Double>?,
        countryCode: String?,
        radius: Double?
    ): List<PlaceEvent> = suspendCoroutine {
        AndroidNetworking.get("$apiDomain$eventDiscoveryUrl")
            .addQueryParameter(FIELD_API_KEY, BuildConfig.ticketmasterKey)
            .addQueryParameter(FIELD_SIZE, FIELD_SIZE_VALUE.toString())
            .addQueryIf(zipCode != null, FIELD_POSTAL_CODE, zipCode)
            .addQueryIf(radius != null, FIELD_RADIUS, radius?.toInt().toString())
            .addQueryIf(radius != null, FIELD_UNIT, FIELD_UNIT_VALUE)
            .addQueryIf(countryCode != null, FIELD_COUNTRY_CODE, countryCode)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val hasEvents = response?.optJSONObject(API_RESPONSE_PAGE_INFO)?.optInt(
                        API_RESPONSE_COUNT, 0
                    ) != 0
                    response?.optJSONObject(API_RESPONSE_CONTAINER)?.optJSONArray(
                        API_RESPONSE_EVENTS
                    )?.let { eventsArray ->
                        val events = (0 until eventsArray.length())
                            .filter {
                                eventsArray.optJSONObject(it) != null
                            }
                            .map {
                                val eventNode = eventsArray.getJSONObject(it)
                                val venuesNode = eventNode.optJSONArray(API_RESPONSE_VENUES)
                                val venueNode = venuesNode?.optJSONObject(0)
                                val datesNode = eventNode.getJSONObject(API_RESPONSE_EVENT_DATES)
                                val startDate =
                                    datesNode.getJSONObject(API_RESPONSE_EVENT_DATE_START)
                                PlaceEvent(
                                    name = eventNode.getString(API_RESPONSE_EVENT_NAME),
                                    location = venueNode?.optString(API_RESPONSE_VENUE_NAME) ?: "",
                                    startDate = startDate.getString(
                                        API_RESPONSE_EVENT_DATE_START_DATETIME
                                    ).toDate(),
                                    url = eventNode.optString(API_RESPONSE_EVENT_URL, "")
                                )
                            }
                        it.resumeWith(Result.success(events))
                    }
                }

                override fun onError(anError: ANError?) {
                    anError?.printStackTrace()
                    Log.e("TicketMasterApi", "request error: $anError")
                    it.resumeWithException(anError ?: Exception("Something happened"))
                }
            })
    }
}