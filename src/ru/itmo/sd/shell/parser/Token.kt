package ru.itmo.sd.shell.parser

enum class Token(val value: String) {
    TEXT("<TEXT>"),
    PIPE("|"),
    LET("let"),
    ASSIGN("="),
    END("<EOF>");

    companion object {
        fun fromText(text: String): Token? =
            values().firstOrNull { it.value == text }
    }
}
