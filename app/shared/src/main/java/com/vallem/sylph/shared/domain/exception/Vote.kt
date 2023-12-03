package com.vallem.sylph.shared.domain.exception

class VoteSavingException : SylphException("O voto não pode ser salvo")

class VoteCountRetrievalException : SylphException("O número de votos não pode ser carregado")
