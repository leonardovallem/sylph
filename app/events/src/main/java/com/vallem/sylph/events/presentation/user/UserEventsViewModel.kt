package com.vallem.sylph.events.presentation.user

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserEventsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
) : ViewModel() {
    val currentUser: FirebaseUser?
        get() = auth.currentUser
}