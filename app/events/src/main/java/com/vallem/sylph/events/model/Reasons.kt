package com.vallem.sylph.events.model

enum class SafetyReason(val label: String) {
    WellLit("Bem iluminado"),
    SecurityCameras("Câmeras de segurança"),
    WellGuard("Bem policiado"),
    FriendlyEstablishmentsAround("Estabelecimentos LGTQIAP+"),
    Other("Outra coisa");

    companion object {
        val values = values()
    }
}

enum class DangerReason(val label: String) {
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
