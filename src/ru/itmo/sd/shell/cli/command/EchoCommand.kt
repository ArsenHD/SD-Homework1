package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult

class EchoCommand(
    override val arguments: List<String>
) : CliSimpleCommand() {

    override val name: String = "echo"

    override fun execute(): ExecutionResult {
        val result = arguments.joinToString(separator = " ")
        writeLine(result)
        return ExecutionResult.OK
    }
}
