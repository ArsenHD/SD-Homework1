package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult

class ExternalCommand(
    override val name: String,
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override fun execute(): ExecutionResult {
        val process = Runtime.getRuntime().exec("${this@ExternalCommand}")
        val error = process.errorStream.reader().readText()
        if (error.isNotEmpty()) {
            errorWriteLine("Error: $error")
            return ExecutionResult.FAILURE
        }
        write(process.inputStream.reader().readText())
        return ExecutionResult.OK
    }

    override fun toString(): String {
        val args = arguments.joinToString(" ")
        return buildString {
            append(name)
            if (args.isNotEmpty()) {
                append(" ")
                append(args)
            }
        }
    }
}
