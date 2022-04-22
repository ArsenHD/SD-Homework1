package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.exception.ShellShutdownException

class ExitCommand(override val arguments: List<String>) : CliSimpleCommand() {

    override val name: String = "exit"

    override fun execute(): ExecutionResult {
        println("*** Exiting shell ***")
        // CommandProcessor will catch this exception and stop the application
        throw ShellShutdownException()
    }
}
