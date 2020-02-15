package com.pmvb.simpleeventsearch.data.base

import java.io.Serializable
import java.util.*

data class PlaceEvent(
    val name: String,
    val description: String? = null,
    val location: String,
    val startDate: Date,
    val endDate: Date? = null,
    val url: String? = null
) : Serializable
