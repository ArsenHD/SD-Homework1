package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import kotlin.system.exitProcess

class ExitCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "exit"

    override fun execute(): ExecutionResult {
        println("*** Exiting shell ***")
        exitProcess(0)
    }
}
