package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.execution
import ru.itmo.sd.shell.environment.Environment
import java.io.File
import java.io.FileNotFoundException

class LsCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "ls"

    override fun execute(env: Environment): ExecutionResult = execution {

        val path = File(if (arguments.isEmpty()) {
            env.getPwd()
        } else {
            arguments[0]
        })

        if (!path.exists() || !path.isDirectory) {
            code = ReturnCode.FAILURE
            exception = FileNotFoundException(path.path)
            return@execution
        }

        for (file in path.listFiles()!!) {
            writeLine(file.name)
        }
    }
}
