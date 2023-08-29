package com.vallem.sylph.shared.util

interface EnumCompanion<T> {
    val values: List<T>
    operator fun get(name: String): T?
}