package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.runCommand
import kotlin.concurrent.thread

class PipelineCommand(val left: CliCommand, val right: CliSimpleCommand) : CliCommand() {
    override fun execute(): ExecutionResult {
        left.connectTo(right)
        val leftRun = thread { runCommand(left) }
        val result = thread { runCommand(right) }
        leftRun.join()
        result.join()
        // TODO: process errors
        return ExecutionResult(ReturnCode.OK)
    }
}
