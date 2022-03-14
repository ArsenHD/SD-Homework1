package ru.itmo.sd.shell.parser

class CommandLexer(private val input: String) {
    lateinit var currentToken: Token
    lateinit var currentText: String
    var currentPos: Int = 0

    private var shouldSkipWhitespaces: Boolean = true

    fun nextToken() {
        currentPos = 0

        skipWhitespaces()

        if (currentPos >= input.length) {
            currentToken = Token.END
            return
        }

        val char = input[currentPos]

        if (char == DASH) {
            shouldSkipWhitespaces = false
            currentToken = Token.DASH
            currentText = DASH.toString()
            currentPos++
            return
        }

        if (char == ASSIGN) {
            currentToken = Token.ASSIGN
            currentText = ASSIGN.toString()
            currentPos++
            return
        }

        val word = nextWord()
        currentText = word
        currentToken = when (word) {
            CAT -> Token.CAT
            ECHO -> Token.ECHO
            WC -> Token.WC
            PWD -> Token.PWD
            EXIT -> Token.EXIT
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
        while (currentPos < input.length && !input[currentPos].isWhitespace()) {
            currentPos++
        }
        return input.substring(start until currentPos)
    }

    companion object {
        private const val CAT = "cat"
        private const val ECHO = "echo"
        private const val WC = "wc"
        private const val PWD = "pwd"
        private const val EXIT = "exit"
        private const val PIPE = "|"
        private const val LET = "let"
        private const val DASH = '-'
        private const val ASSIGN = '='
    }
}
