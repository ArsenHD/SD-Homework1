package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.execution

class EchoCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "echo"

    override fun execute(): ExecutionResult = execution {
        val result = arguments.joinToString(separator = " ")
        writeLine(result)
    }
}
