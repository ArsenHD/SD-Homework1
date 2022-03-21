package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.execution

class EchoCommand(
    override val options: List<Option> = emptyList(),
    override val arguments: List<String> = emptyList()
) : CliBuiltinCommand() {

    override val name: String = "echo"

    override fun execute(input: String?): ExecutionResult = execution {
        val result = arguments.joinToString(" ")
        output.append(result)
    }
}
