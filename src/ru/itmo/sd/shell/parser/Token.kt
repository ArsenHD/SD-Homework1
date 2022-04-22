package ru.itmo.sd.shell.parser

enum class Token(val value: String) {
    TEXT("<TEXT>"),
    PIPE("|"),
    LET("LET"),
    ASSIGN("="),
    END("<EOF>")
}
