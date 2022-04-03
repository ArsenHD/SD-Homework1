package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.runCommand
import kotlin.concurrent.thread

class PipelineCommand(val left: CliCommand, val right: CliSimpleCommand) : CliCommand() {
    init {
        left.connectTo(right)
    }

    override fun connectTo(command: CliCommand) {
        right.connectTo(command)
    }

    override fun execute(): ExecutionResult {
        val leftRun = thread { runCommand(left) }
        val result = thread { runCommand(right) }
        leftRun.join()
        result.join()
        // TODO: process errors
        return ExecutionResult(ReturnCode.OK)
    }
}
