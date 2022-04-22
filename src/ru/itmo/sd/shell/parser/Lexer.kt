package ru.itmo.sd.shell.parser

interface Lexer {
    val currentToken: Token
    val currentText: String
    fun advance(expected: Token? = null): Boolean
}