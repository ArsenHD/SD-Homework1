package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.cli.command.*
import ru.itmo.sd.shell.cli.util.runCommand
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.parser.CommandParser

class CommandProcessor {
    private val environment: Environment = Environment()
    private val parser: CommandParser = CommandParser()

    fun run() {
        var line = readLine()
        while (line != null) {
            val expandedLine = substituteVariables(line)
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
                val output = runCommand(cliElement)
                print(output)
            }
        }
    }.onFailure {
        println("Error: failed to execute command")
    }

    private fun finish() {
        println("*** Stopping shell ***")
    }

    private fun substituteVariables(text: String): String =
        text.split(" ").joinToString(" ") { item ->
            when {
                item.startsWith("$") -> {
                    val name = item.drop(1)
                    environment[name] ?: item
                }
                else -> item
            }
        }
}