package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.environment.Environment
import kotlin.system.exitProcess

class ExitCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "exit"

    override fun execute(env: Environment): ExecutionResult {
        println("*** Exiting shell ***")
        exitProcess(0)
    }
}
