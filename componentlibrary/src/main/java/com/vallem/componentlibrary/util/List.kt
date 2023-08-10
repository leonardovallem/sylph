package com.vallem.componentlibrary.util

fun <T> optionalListOf(condition: Boolean, value: () -> T) =
    if (condition) listOf(value())
    else emptyList()

fun <T> optionalListOf(vararg values: Pair<T, Boolean>) = values.filter { it.second }

fun <T> Map<T, Boolean>.asOptionalList() = filter { (_, valid) -> valid }.map { it.key }
