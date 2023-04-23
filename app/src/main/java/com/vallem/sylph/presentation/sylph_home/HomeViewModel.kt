package com.vallem.sylph.presentation.sylph_home

import android.location.Location
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mapbox.android.core.location.LocationEngine
import com.vallem.sylph.data.datastore.AppSettings
import com.vallem.sylph.util.extensions.mapCenter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    val dataStore: DataStore<AppSettings>,
    val locationEngine: LocationEngine,
) : ViewModel() {
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    fun saveCurrentLocation(location: Location) = viewModelScope.launch {
        dataStore.updateData {
            it.copy(mapCenter = location.mapCenter)
        }
    }
}