package com.vallem.sylph.shared.domain.model.event

import com.vallem.sylph.shared.util.EnumCompanion

enum class DangerVictim {
    User, OtherPerson;

    companion object : EnumCompanion<DangerVictim> {
        override val values = values().toList()
        override operator fun get(name: String) = values.firstOrNull { it.name == name }
    }
}