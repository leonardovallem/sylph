package com.vallem.sylph.shared.extensions

import com.vallem.sylph.shared.domain.exception.SylphException

fun Exception.getSylphExceptionMessage(fallback: String) =
    (this as? SylphException)?.message ?: fallback