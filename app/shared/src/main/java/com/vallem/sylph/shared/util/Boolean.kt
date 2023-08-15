package com.vallem.sylph.shared.util

fun <T> truthyCallback(callback: (T) -> Unit): (T) -> Boolean = {
    callback(it)
    true
}

fun <T> falsyCallback(callback: (T) -> Unit): (T) -> Boolean = {
    callback(it)
    false
}
