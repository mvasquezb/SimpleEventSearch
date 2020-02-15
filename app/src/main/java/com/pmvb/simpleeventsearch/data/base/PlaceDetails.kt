package com.pmvb.simpleeventsearch.data.base

data class PlaceDetails(
    val placeId: String,
    val address: String,
    val name: String,
    val latLng: Pair<Double, Double>,
    val zipCode: String,
    val city: String,
    val country: String
)