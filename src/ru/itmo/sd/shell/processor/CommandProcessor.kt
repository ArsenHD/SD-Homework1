package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.cli.command.*
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.exception.ShellShutdownException
import ru.itmo.sd.shell.exception.SyntaxError
import ru.itmo.sd.shell.parser.CommandLexer
import ru.itmo.sd.shell.parser.CommandParser
import java.io.InputStream
import java.io.OutputStream

class CommandProcessor(inputStream: InputStream, outputStream: OutputStream) {
    private val environment = Environment()

    private val lexer = CommandLexer(
        reader = inputStream.bufferedReader(),
        handler = CommandHandler(environment)
    )

    private val parser = CommandParser(inputStream, outputStream, lexer)

    fun run() = runCatching {
        var element = parser.parse()
        while (element != null) {
            process(element)
            element = parser.parse()
        }
        finish()
    }.onFailure {
        when (it) {
            is SyntaxError -> println("shell: syntax error: ${it.message}")
            is ShellShutdownException -> Unit
            is Exception -> println("shell: error: failed to execute command")
        }
    }

    private fun process(cliElement: CliElement) {
        when (cliElement) {
            is CliVariableAssignment -> {
                val (name, value) = cliElement
                environment[name] = value
            }
            is CliEmptyLine -> Unit
            is CliCommand -> cliElement.execute()
        }
    }

    private fun finish() {
        println("*** Stopping shell ***")
    }
}
