package com.vallem.init.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    private val _picUrl = MutableStateFlow<String?>(null)
    val picUrl = _picUrl.asStateFlow()

    fun setPicUrl(url: String) = _picUrl.update { url }
}
