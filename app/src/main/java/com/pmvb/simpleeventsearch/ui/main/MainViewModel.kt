package com.pmvb.simpleeventsearch.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmvb.simpleeventsearch.data.base.PlaceResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
class MainViewModel : ViewModel() {
    private val queryChannel = ConflatedBroadcastChannel<String>()

    private val _placeResults = MutableLiveData<List<PlaceResult>>()
    val placeResults: LiveData<List<PlaceResult>> = _placeResults

    init {
        queryChannel
            .asFlow()
            .debounce(QUERY_SPAN)
            .onEach { searchPlace(it) }
            .launchIn(viewModelScope)
    }

    private fun searchPlace(query: String) {
        _placeResults.value = listOf()
    }

    fun onQuery(query: String) {
        queryChannel.offer(query)
    }

    companion object {
        const val QUERY_SPAN = 500L
    }
}
