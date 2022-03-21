package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.runCommand

class PipelineCommand(val left: CliCommand, val right: CliSimpleCommand) : CliCommand() {
    override fun execute(input: String?): ExecutionResult {
        val leftOutput = runCommand(left, input)
        return right.execute(leftOutput)
    }
}
