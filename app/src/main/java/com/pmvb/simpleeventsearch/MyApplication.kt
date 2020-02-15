package com.pmvb.simpleeventsearch

import android.app.Application
import com.androidnetworking.AndroidNetworking
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Places.initialize(this, BuildConfig.googleApiKey)
        placesClient = Places.createClient(this)

        AndroidNetworking.initialize(applicationContext)
        AndroidNetworking.enableLogging()
    }

    companion object {
        lateinit var placesClient: PlacesClient
    }
}