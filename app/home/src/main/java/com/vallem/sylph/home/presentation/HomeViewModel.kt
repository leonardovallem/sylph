package com.vallem.sylph.home.presentation

import android.location.Location
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mapbox.geojson.FeatureCollection
import com.vallem.sylph.shared.data.datastore.AppSettings
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.EventsRepository
import com.vallem.sylph.shared.extensions.mapCenter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val eventsRepository: EventsRepository,
    private val dataStore: DataStore<AppSettings>,
) : ViewModel() {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    private val _eventsFeatures = MutableStateFlow<Result<FeatureCollection>>(Result.Loading)
    val eventsFeatures = _eventsFeatures.asStateFlow()

    init {
        updateEvents()
    }

    fun updateEvents() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = eventsRepository.retrieveEventsFeatures()
            _eventsFeatures.update { result }
        }
    }

    fun saveCurrentLocation(location: Location) = viewModelScope.launch {
        dataStore.updateData {
            it.copy(mapCenter = location.mapCenter)
        }
    }
}