package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.execution

class PwdCommand(
    override val options: List<Option> = emptyList(),
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "pwd"

    override fun execute(input: String?): ExecutionResult = execution {
        writeLine(System.getProperty("user.dir"))
    }
}
