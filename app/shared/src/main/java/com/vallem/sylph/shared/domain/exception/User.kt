package com.vallem.sylph.shared.domain.exception

class UserNotFoundException : SylphException("O usuário não foi encontrado")

class UserSavingException : SylphException("O usuário não pode ser salvo")