package com.vallem.sylph.shared.util

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.OpenResultRecipient

class EmptyOpenResultRecipient<R> : OpenResultRecipient<R> {
    @Composable
    override fun onNavResult(listener: (NavResult<R>) -> Unit) = Unit
}