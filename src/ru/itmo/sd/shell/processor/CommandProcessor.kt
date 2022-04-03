package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.cli.command.*
import ru.itmo.sd.shell.cli.util.runCommand
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.parser.CommandParser

class CommandProcessor {
    private val environment: Environment = Environment()
    private val parser: CommandParser = CommandParser()
    private val handler: CommandHandler = CommandHandler(environment)

    fun run() {
        var line = readLine()
        while (line != null) {
            val expandedLine = handler.substituteVariables(line)
            if (expandedLine.isNotEmpty()) {
                val element = parser.parse(expandedLine)
                process(element)
            }
            line = readLine()
        }
        finish()
    }

    private fun process(cliElement: CliElement) = runCatching {
        when (cliElement) {
            is CliVariableAssignment -> {
                val (name, value) = cliElement
                environment[name] = value
            }
            is CliCommand -> {
                runCommand(cliElement)
            }
        }
    }.onFailure {
        println("Error: failed to execute command")
    }

    private fun finish() {
        println("*** Stopping shell ***")
    }
}
