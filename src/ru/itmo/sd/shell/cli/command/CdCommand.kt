package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.execution
import ru.itmo.sd.shell.environment.Environment
import java.io.File

class CdCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "cd"

    override fun execute(env: Environment): ExecutionResult = execution {
        if (arguments.isNotEmpty()) {
            val path = File(arguments[0])

            if (path.exists() && path.isDirectory) {
                env.setPwd(arguments[0])
            }
        }
    }
}
