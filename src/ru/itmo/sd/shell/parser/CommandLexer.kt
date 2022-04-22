package ru.itmo.sd.shell.parser

class CommandLexer(private val input: String) {
    lateinit var currentToken: Token
    lateinit var currentText: String
    var currentPos: Int = 0

    private var shouldSkipWhitespaces: Boolean = true

    fun nextToken() {
        skipWhitespaces()

        if (currentPos >= input.length) {
            currentToken = Token.END
            return
        }

        val char = input[currentPos]

        if (char == ASSIGN) {
            currentToken = Token.ASSIGN
            currentText = ASSIGN.toString()
            currentPos++
            return
        }

        val word = nextWord()
        currentText = word
        currentToken = when (word) {
            PIPE -> Token.PIPE
            LET -> Token.LET
            else -> Token.TEXT
        }

        shouldSkipWhitespaces = true
    }

    private fun skipWhitespaces() {
        if (!shouldSkipWhitespaces) {
            return
        }
        while (currentPos < input.length && input[currentPos].isWhitespace()) {
            currentPos++
        }
    }

    private fun nextWord(): String {
        val start = currentPos
        currentPos++
        while (currentPos < input.length && !input[currentPos].isWhitespace() && input[currentPos] != '=') {
            currentPos++
        }
        return input.substring(start until currentPos)
    }

    companion object {
        private const val PIPE = "|"
        private const val LET = "let"
        private const val ASSIGN = '='
    }
}
