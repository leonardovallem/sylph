package com.vallem.sylph.shared.domain.model.event

import com.vallem.sylph.shared.util.EnumCompanion

interface Reason {
    val label: String
    val enumName: String
}

enum class SafetyReason(override val label: String) : Reason {
    WellLit("Bem iluminado"),
    SecurityCameras("Câmeras de segurança"),
    WellGuard("Bem policiado"),
    FriendlyEstablishmentsAround("Estabelecimentos LGTQIAP+"),
    Other("Outra coisa");

    override val enumName = name

    companion object : EnumCompanion<SafetyReason> {
        override val values = values().toList()
        override operator fun get(name: String) = values.firstOrNull { it.name == name }
    }
}

enum class DangerReason(override val label: String) : Reason {
    Battery("Agressão"),
    SexualHarassment("Assédio sexual"),
    MoralHarassment("Assédio moral"),
    Cursing("Xingamentos"),
    Discrimination("Discriminação"),
    Other("Outra coisa");

    override val enumName = name

    companion object : EnumCompanion<DangerReason> {
        override val values = values().toList()
        override operator fun get(name: String) = values.firstOrNull { it.name == name }
    }
}
