package com.vallem.sylph.events.model

interface Reason {
    val label: String
}

enum class SafetyReason(override val label: String) : Reason {
    WellLit("Bem iluminado"),
    SecurityCameras("Câmeras de segurança"),
    WellGuard("Bem policiado"),
    FriendlyEstablishmentsAround("Estabelecimentos LGTQIAP+"),
    Other("Outra coisa");

    companion object {
        val values = values()
    }
}

enum class DangerReason(override val label: String) : Reason {
    Battery("Agressão"),
    SexualHarassment("Assédio sexual"),
    MoralHarassment("Assédio moral"),
    Cursing("Xingamentos"),
    Discrimination("Discriminação"),
    Other("Outra coisa");

    companion object {
        val values = values()
    }
}
