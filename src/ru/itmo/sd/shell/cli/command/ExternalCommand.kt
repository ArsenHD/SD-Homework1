package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.execution

class ExternalCommand(
    private val command: String,
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {
    override val name: String = command

    override fun execute(): ExecutionResult = execution {
        val process = Runtime.getRuntime().exec("${this@ExternalCommand}")
        val returnCode = process.waitFor()
        val error = process.errorStream.reader().readText()
        if (error.isNotEmpty()) {
            errorWriteLine("Error: $error")
            code = returnCode
            return@execution
        }
        write(process.inputStream.reader().readText())
    }

    override fun toString(): String {
        val args = arguments.joinToString(" ")
        return buildString {
            append(command)
            if (args.isNotEmpty()) {
                append(" ")
                append(args)
            }
        }
    }
}
