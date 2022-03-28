package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.execution

class ExternalCommand(
    private val command: String,
    override val options: List<Option> = emptyList(),
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {
    override val name: String = command

    override fun execute(input: String?): ExecutionResult = execution {
        val process = Runtime.getRuntime().exec("${this@ExternalCommand}")
        val returnCode = process.waitFor()
        val error = process.errorStream.reader().readText()
        if (error.isNotEmpty()) {
            output.appendLine("Error: $error")
            code = returnCode
            return@execution
        }
        output.append(process.inputStream.reader().readText())
    }

    override fun toString(): String {
        val opts = options.joinToString(" ") { "-${it.value}" }
        val args = arguments.joinToString(" ")
        return buildString {
            append(command)
            if (opts.isNotEmpty()) {
                append(" ")
                append(opts)
            }
            if (args.isNotEmpty()) {
                append(" ")
                append(args)
            }
        }
    }
}
