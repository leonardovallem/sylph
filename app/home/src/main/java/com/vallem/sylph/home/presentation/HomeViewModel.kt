package com.vallem.sylph.home.presentation

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mapbox.geojson.FeatureCollection
import com.mapbox.maps.CameraState
import com.vallem.sylph.shared.data.datastore.AppSettings
import com.vallem.sylph.shared.data.datastore.serializable
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    val mapCameraState = dataStore.data.map { it.cameraState }

    init {
        updateEvents()
    }

    fun updateEvents() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val result = eventsRepository.retrieveEventsFeatures()
            _eventsFeatures.update { result }
        }
    }

    fun saveMapCameraState(cameraState: CameraState) = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            dataStore.updateData {
                it.copy(cameraState = cameraState.serializable())
            }
        }
    }
}