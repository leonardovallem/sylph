package com.vallem.sylph.home.presentation

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventsRepository: EventsRepository,
    private val dataStore: DataStore<AppSettings>,
) : ViewModel() {
    private val _eventsFeatures = MutableStateFlow<Result<FeatureCollection>>(Result.Loading)
    val eventsFeatures = _eventsFeatures.asStateFlow()

    val mapCameraState = dataStore.data.map { it.cameraState }

    init {
        updateEvents()
    }

    fun updateEvents() = viewModelScope.launch {
        _eventsFeatures.value = Result.Loading
        withContext(Dispatchers.IO) {
            _eventsFeatures.value = eventsRepository.retrieveEventsFeatures()
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