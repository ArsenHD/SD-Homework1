package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult

class PwdCommand(
    override val arguments: List<String>
) : CliSimpleCommand() {

    override val name: String = "pwd"

    override fun execute(): ExecutionResult {
        writeLine(System.getProperty("user.dir"))
        return ExecutionResult.OK
    }
}
