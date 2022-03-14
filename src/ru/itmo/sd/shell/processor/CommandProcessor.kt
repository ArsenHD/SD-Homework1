package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.command.CliCommand
import ru.itmo.sd.shell.command.Command
import ru.itmo.sd.shell.command.VariableAssignment
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.parser.CommandParser

class CommandProcessor {
    private val environment: Environment = Environment()
    private val parser: CommandParser = CommandParser()

    fun run() {
        var line = readLine()
        while (line != null) {
            parser.parse(line)
            line = readLine()
        }
        finish()
    }

    private fun process(command: Command) {
        when (command) {
            is VariableAssignment -> {
                val (name, value) = command
                environment[name] = value
            }
            is CliCommand -> command.execute()
        }
    }

    private fun finish() {
        println("*** Stopping shell ***")
    }
}