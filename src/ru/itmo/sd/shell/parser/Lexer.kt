package ru.itmo.sd.shell.parser

interface Lexer {
    val currentToken: Token
    val currentText: String

    /**
     * Consume current token and move forward.
     */
    fun advance(expected: Token? = null): Boolean
}