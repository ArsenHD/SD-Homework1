package ru.itmo.sd.shell.parser

import ru.itmo.sd.shell.cli.command.CatCommand
import ru.itmo.sd.shell.cli.command.CdCommand
import ru.itmo.sd.shell.cli.command.CliCommand
import ru.itmo.sd.shell.cli.command.CliElement
import ru.itmo.sd.shell.cli.command.CliSimpleCommand
import ru.itmo.sd.shell.cli.command.CliVariableAssignment
import ru.itmo.sd.shell.cli.command.EchoCommand
import ru.itmo.sd.shell.cli.command.ExitCommand
import ru.itmo.sd.shell.cli.command.ExternalCommand
import ru.itmo.sd.shell.cli.command.GrepCommand
import ru.itmo.sd.shell.cli.command.LsCommand
import ru.itmo.sd.shell.cli.command.PipelineCommand
import ru.itmo.sd.shell.cli.command.PwdCommand
import ru.itmo.sd.shell.cli.command.WcCommand
import ru.itmo.sd.shell.exception.UnexpectedTokenException

class CommandParser {
    private lateinit var lexer: CommandLexer

    private val currentToken: Token
        get() = lexer.currentToken

    fun parse(input: String): CliElement {
        lexer = CommandLexer(input)
        lexer.nextToken()
        return when (currentToken) {
            Token.LET -> parseAssignment()
            else -> parseCliCommand()
        }
    }

    private fun parseAssignment(): CliVariableAssignment {
        require(currentToken == Token.LET) { unexpectedToken("'let'") }

        lexer.nextToken()
        require(currentToken == Token.TEXT) { unexpectedToken("variable name") }
        val name = lexer.currentText

        lexer.nextToken()
        require(currentToken == Token.ASSIGN) { unexpectedToken("'='") }

        lexer.nextToken()
        val value = lexer.currentText

        return CliVariableAssignment(name, value)
    }

    private fun parseCliCommand(): CliCommand {
        var command: CliCommand = parseSimpleCommand()
        while (true) {
            when (currentToken) {
                Token.PIPE -> {
                    lexer.nextToken()
                    command = PipelineCommand(command, parseSimpleCommand())
                }
                else -> return command
            }
        }
    }

    private fun parseSimpleCommand(): CliSimpleCommand {
        val buildCommand = when (currentToken) {
            Token.CAT -> ::CatCommand
            Token.ECHO -> ::EchoCommand
            Token.WC -> ::WcCommand
            Token.PWD -> ::PwdCommand
            Token.CD -> ::CdCommand
            Token.LS -> ::LsCommand
            Token.GREP -> ::GrepCommand
            Token.EXIT -> ::ExitCommand
            Token.TEXT -> {
                val name = lexer.currentText
                { args -> ExternalCommand(name, args) }
            }
            else -> throw UnexpectedTokenException(currentToken)
        }
        lexer.nextToken()
        val arguments = parseArguments()
        return buildCommand(arguments)
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
