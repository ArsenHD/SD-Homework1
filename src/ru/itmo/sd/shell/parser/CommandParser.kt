package ru.itmo.sd.shell.parser

import ru.itmo.sd.shell.command.*

class CommandParser {
    private lateinit var lexer: CommandLexer

    private val currentToken: Token
        get() = lexer.currentToken

    fun parse(input: String): Command {
        lexer = CommandLexer(input)
        lexer.nextToken()
        return when (currentToken) {
            Token.LET -> parseAssignment()
            else -> parseCliCommand()
        }
    }

    private fun parseAssignment(): VariableAssignment {
        require(currentToken == Token.LET) { unexpectedToken("'let'") }

        lexer.nextToken()
        require(currentToken == Token.TEXT) { unexpectedToken("variable name") }
        val name = lexer.currentText

        lexer.nextToken()
        require(currentToken == Token.ASSIGN) { unexpectedToken("'='") }

        lexer.nextToken()
        require(currentToken == Token.TEXT) { unexpectedToken("variable value") }
        val value = lexer.currentText

        return VariableAssignment(name, value)
    }

    private fun parseCliCommand(): CliCommand {
        var command = parseSingleCommand()
        while (true) {
            when (currentToken) {
                Token.PIPE -> {
                    lexer.nextToken()
                    command = PipelineCommand(command, parseSingleCommand())
                }
                else -> return command
            }
        }
    }

    private fun parseSingleCommand(): CliCommand {
        val command: CliCommand = when (currentToken) {
            Token.CAT -> CatCommand()
            Token.ECHO -> EchoCommand()
            Token.WC -> WcCommand()
            Token.PWD -> PwdCommand()
            Token.EXIT -> ExitCommand()
            Token.TEXT -> ExternalCommand(lexer.currentText)
            else -> error("Unexpected token $currentToken")
        }

        lexer.nextToken()
        command.options += parseOptions()
        command.arguments += parseArguments()
        return command
    }

    private fun parseOptions(): List<Option> {
        val options = mutableListOf<Option>()
        while (currentToken == Token.DASH) {
            lexer.nextToken()
            require(currentToken == Token.TEXT) { unexpectedToken("option") }
            options += Option(lexer.currentText)
            lexer.nextToken()
        }
        return options
    }

    private fun parseArguments(): List<String> {
        val arguments = mutableListOf<String>()
        while (currentToken == Token.TEXT) {
            arguments += lexer.currentText
            lexer.nextToken()
        }
        return arguments
    }

    private fun unexpectedToken(expected: String) = "Expected $expected, but got: ${lexer.currentText}"
}
