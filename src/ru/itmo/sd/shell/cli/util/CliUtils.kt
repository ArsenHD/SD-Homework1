package ru.itmo.sd.shell.cli.util

import ru.itmo.sd.shell.cli.command.CliCommand

data class Option(val value: String) {
    override fun toString(): String = "-$value"
}

internal object ReturnCode {
    const val OK = 0
    const val FAILURE = 1
}

data class ExecutionResult(val code: Int, val output: String, val exception: Exception? = null)

internal class ExecutionResultBuilder {
    var code: Int = ReturnCode.OK
    var output: StringBuilder = StringBuilder()
    var exception: Exception? = null

    fun buildResult(): ExecutionResult =
        ExecutionResult(code, output.toString(), exception)
}

internal fun execution(block: ExecutionResultBuilder.() -> Unit): ExecutionResult {
    return ExecutionResultBuilder().apply(block).buildResult()
}

fun runCommand(command: CliCommand, input: String? = null): String {
    val (code, output, exception) = command.execute(input)
    if (code != ReturnCode.OK && exception != null) {
        println(exception.message)
    }
    return output
}
