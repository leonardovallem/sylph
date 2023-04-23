package com.vallem.sylph.presentation.sylph_home

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : ViewModel() {
    val currentUser: FirebaseUser?
        get() = auth.currentUser
}