package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import kotlin.concurrent.thread

class PipelineCommand(val left: CliCommand, val right: CliCommand) : CliCommand() {
    init {
        left.connectTo(right)
    }

    override fun connectTo(command: CliCommand) {
        right.connectTo(command)
    }

    override fun execute(): ExecutionResult {
        val leftRun = thread { left.execute() }
        val result = thread { right.execute() }
        leftRun.join()
        result.join()
        // TODO: process errors
        return ExecutionResult.OK
    }
}
