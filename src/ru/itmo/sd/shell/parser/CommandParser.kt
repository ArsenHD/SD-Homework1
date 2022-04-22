package ru.itmo.sd.shell.parser

import ru.itmo.sd.shell.cli.command.CliCommand
import ru.itmo.sd.shell.cli.command.CliElement
import ru.itmo.sd.shell.cli.command.CliEmptyLine
import ru.itmo.sd.shell.cli.command.CliVariableAssignment
import ru.itmo.sd.shell.cli.command.PipelineCommand
import ru.itmo.sd.shell.cli.command.util.CommandFactoryHandler
import ru.itmo.sd.shell.exception.UnexpectedEofException

class CommandParser(private val lexer: Lexer) {

    private val currentToken: Token
        get() = lexer.currentToken

    fun parse(): CliElement? {
        if (!lexer.advance()) {
            return null
        }
        return when (currentToken) {
            Token.LET -> parseAssignment()
            Token.END -> CliEmptyLine
            else -> parseCliCommand()
        }
    }

    private fun parseAssignment(): CliVariableAssignment {
        lexer.advance(Token.LET)
        val name = lexer.currentText

        lexer.advance(Token.TEXT)

        lexer.advance(Token.ASSIGN)
        val value = lexer.currentText

        lexer.advance(Token.TEXT)
        require(currentToken == Token.END) { "Expected end of line after variable declaration" }

        return CliVariableAssignment(name, value)
    }

    private fun parseCliCommand(): CliCommand {
        var command: CliCommand = parseSimpleCommand()
        while (true) {
            when (currentToken) {
                Token.PIPE -> {
                    lexer.advance(Token.PIPE)
                    // if we reached the end of line and there are no further input lines
                    if (currentToken == Token.END && !lexer.advance()) {
                        throw UnexpectedEofException()
                    }
                    command = PipelineCommand(command, parseSimpleCommand())
                }
                else -> return command
            }
        }
    }

    private fun parseSimpleCommand(): CliCommand {
        val commandName = lexer.currentText
        val commandFactory = CommandFactoryHandler.getFactoryFor(commandName)

        lexer.advance()
        val arguments = parseArguments()
        return commandFactory.createCommand(arguments)
    }

    private fun parseArguments(): List<String> {
        val arguments = mutableListOf<String>()
        while (currentToken == Token.TEXT) {
            arguments += lexer.currentText
            lexer.advance()
        }
        return arguments
    }
}
