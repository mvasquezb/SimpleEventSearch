package com.pmvb.simpleeventsearch.data.base

import java.io.Serializable

data class PlaceResult(
    val placeId: String,
    val primaryText: String,
    val secondaryText: String
) : Serializable