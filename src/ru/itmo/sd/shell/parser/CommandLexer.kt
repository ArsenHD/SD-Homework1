package ru.itmo.sd.shell.parser

import ru.itmo.sd.shell.processor.CommandHandler
import java.io.BufferedReader

class CommandLexer(
    private val reader: BufferedReader,
    private val handler: CommandHandler
) : Lexer {
    override lateinit var currentToken: Token
    override lateinit var currentText: String

    private lateinit var currentLine: String

    private var currentPos: Int = 0

    private var shouldSkipWhitespaces: Boolean = true

    override fun advance(expected: Token?): Boolean {
        // if we have just started parsing
        // or reached an end of the current line
        if (currentPos == 0 || currentToken == Token.END) {
            currentLine = reader.readLine() ?: return false
            currentLine = handler.preprocessRawText(currentLine)
            currentPos = 0
        }

        if (expected != null) {
            require(currentToken == expected) { unexpectedToken(expected) }
        }

        skipWhitespaces()

        if (currentPos >= currentLine.length) {
            currentToken = Token.END
            return true
        }

        val char = currentLine[currentPos]

        if (char == ASSIGN) {
            currentToken = Token.ASSIGN
            currentText = ASSIGN.toString()
            currentPos++
            return true
        }

        val word = nextWord()
        currentText = word
        currentToken = when (word) {
            PIPE -> Token.PIPE
            LET -> Token.LET
            else -> Token.TEXT
        }

        shouldSkipWhitespaces = true
        return true
    }

    private fun skipWhitespaces() {
        if (!shouldSkipWhitespaces) {
            return
        }
        while (currentPos < currentLine.length && currentLine[currentPos].isWhitespace()) {
            currentPos++
        }
    }

    private fun nextWord(): String {
        val start = currentPos
        currentPos++
        while (currentPos < currentLine.length && !currentLine[currentPos].isWhitespace() && currentLine[currentPos] != '=') {
            currentPos++
        }
        return currentLine.substring(start until currentPos)
    }

    private fun unexpectedToken(expected: Token) = "Expected ${expected.value}, but got: $currentText"

    companion object {
        private const val PIPE = "|"
        private const val LET = "let"
        private const val ASSIGN = '='
    }
}
