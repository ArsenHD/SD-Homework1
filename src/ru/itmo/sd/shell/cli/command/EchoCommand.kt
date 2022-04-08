package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.execution
import ru.itmo.sd.shell.environment.Environment

class EchoCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "echo"

    override fun execute(env: Environment): ExecutionResult = execution {
        val result = arguments.joinToString(separator = " ", postfix = "\n")
        write(result)
    }
}
