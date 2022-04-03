package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import kotlin.system.exitProcess

class ExitCommand(
    override val options: List<Option> = emptyList(),
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "exit"

    override fun execute(input: String?): ExecutionResult {
        println("*** Exiting shell ***")
        exitProcess(0)
    }
}
