package com.vallem.sylph.events.presentation.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.vallem.sylph.shared.domain.model.Result
import com.vallem.sylph.shared.domain.model.event.Event
import com.vallem.sylph.shared.domain.repository.EventsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val eventsRepository: EventsRepository
) : ViewModel() {
    var eventSaveResult by mutableStateOf<Result<Unit>?>(null)

    fun saveEvent(event: Event) {
        if (auth.currentUser == null) {
            eventSaveResult = Result.Failure(
                FirebaseNoSignedInUserException("Current user is null")
            )

            return
        }

        eventSaveResult = Result.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventsRepository.saveEvent(event.update(userId = auth.currentUser?.uid.orEmpty()))
                    .onSuccess {
                        eventSaveResult = Result.Success(Unit)
                    }
                    .onFailure {
                        eventSaveResult = Result.Failure(it)
                    }
            }
        }
    }
}