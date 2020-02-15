package com.pmvb.simpleeventsearch.data.base

import java.io.Serializable
import java.util.*

data class PlaceEvent(
    val name: String,
    val description: String,
    val location: String,
    val startDate: Date,
    val endDate: Date? = null
) : Serializable
