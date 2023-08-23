package com.vallem.sylph.events.presentation.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
class UserEventsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val eventsRepository: EventsRepository,
) : ViewModel() {
    var eventsQueryResult by mutableStateOf<Result<List<Event>>>(Result.Loading)

    val currentUser: FirebaseUser?
        get() = auth.currentUser

    init {
        retrieveEvents()
    }

    fun retrieveEvents() {
        if (currentUser == null) {
            eventsQueryResult = Result.Failure(
                FirebaseNoSignedInUserException("Current user is null")
            )

            return
        }

        eventsQueryResult = Result.Loading
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                eventsQueryResult = eventsRepository.retrieveUserEvents(currentUser!!.uid)
            }
        }
    }
}