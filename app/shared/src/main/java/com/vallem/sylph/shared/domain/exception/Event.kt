package com.vallem.sylph.shared.domain.exception

class EventNotFoundException : SylphException("O evento não foi encontrado")

class UserEventsRetrievalException : SylphException("Os eventos do usuário não foram encontrados")

class EventFeaturesRetrievalException : SylphException("Os eventos do mapa não foram encontrados")

class EventSavingException : SylphException("O evento não pode ser salvo")