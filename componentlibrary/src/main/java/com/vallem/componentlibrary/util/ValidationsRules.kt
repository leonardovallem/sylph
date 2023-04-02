package com.vallem.componentlibrary.util

enum class PasswordToken {
    UppercaseLetter, LowercaseLetter, Number, Symbol;

    companion object {
        fun tokenize(input: String): Map<PasswordToken, Int> {
            val tokens = values().associateWith { 0 }.toMutableMap()

            input.forEach {
                if (it.isDigit()) tokens[Number] = tokens[Number]!! + 1
                else if (it.isUpperCase()) tokens[UppercaseLetter] = tokens[UppercaseLetter]!! + 1
                else if (it.isLowerCase()) tokens[LowercaseLetter] = tokens[LowercaseLetter]!! + 1
                else tokens[Symbol] = tokens[Symbol]!! + 1
            }

            return tokens
        }
    }
}

sealed class ValidationRule(val tokens: Map<PasswordToken, Int>, val size: IntRange) {
    object Password : ValidationRule(
        tokens = mapOf(
            PasswordToken.LowercaseLetter to 1,
            PasswordToken.UppercaseLetter to 1,
            PasswordToken.Number to 1,
            PasswordToken.Symbol to 1,
        ),
        size = 8..40
    ) {
        override fun isValid(input: String) = run {
            val inputTokens = PasswordToken.tokenize(input)
            input.length in size && inputTokens.all { (token, count) -> count >= tokens[token]!! }
        }

        override fun helperTextFor(input: String): String? {
            fun notMetTokenNecessityLine(
                current: Int,
                required: Int,
                vararg subjects: String
            ): String {
                return "\n${current}/${required} ${subjects.pluralize(current)}"
            }

            val requiredNumbers = tokens[PasswordToken.Number]!!
            val requiredUppercase = tokens[PasswordToken.UppercaseLetter]!!
            val requiredLowercase = tokens[PasswordToken.LowercaseLetter]!!
            val requiredSymbols = tokens[PasswordToken.Symbol]!!

            val inputTokens = PasswordToken.tokenize(input)
            if (isValid(input)) return null

            val foundNumbers = inputTokens[PasswordToken.Number]!!
            val foundUppercase = inputTokens[PasswordToken.UppercaseLetter]!!
            val foundLowercase = inputTokens[PasswordToken.LowercaseLetter]!!
            val foundSymbols = inputTokens[PasswordToken.Symbol]!!

            var helperText = "Sua senha precisa conter:"

            if (input.length !in size) helperText += "\n de ${size.first} a ${size.last} caracteres"
            if (foundNumbers < requiredNumbers) helperText += notMetTokenNecessityLine(
                foundNumbers,
                requiredNumbers,
                "número"
            )
            if (foundUppercase < requiredUppercase) helperText += notMetTokenNecessityLine(
                foundUppercase,
                requiredUppercase,
                "letra",
                "maiúscula"
            )
            if (foundLowercase < requiredLowercase) helperText += notMetTokenNecessityLine(
                foundLowercase,
                requiredLowercase,
                "letra",
                "minúscula"
            )
            if (foundSymbols < requiredSymbols) helperText += notMetTokenNecessityLine(
                foundSymbols,
                requiredSymbols,
                "símbolo"
            )

            return helperText
        }
    }

    abstract fun isValid(input: String): Boolean
    abstract fun helperTextFor(input: String): String?
}
