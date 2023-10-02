package com.vallem.sylph.shared.domain.model

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val e: Exception) : Result<Nothing>()

    fun onSuccess(action: (T) -> Unit) = apply {
        if (this is Success) action(data)
    }

    fun onFailure(action: (Exception) -> Unit) = apply {
        if (this is Failure) action(e)
    }

    fun getOrNull() = (this as? Success)?.data

    val isSuccess: Boolean
        get() = this is Success

    val isLoading: Boolean
        get() = this is Loading
}