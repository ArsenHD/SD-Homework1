package ru.itmo.sd.shell.cli.util

import ru.itmo.sd.shell.cli.command.CliCommand

data class Option(val name: String, val values: List<String>) {
    override fun toString(): String = "-$name ${values.joinToString(" ")}"
}

internal object ReturnCode {
    const val OK = 0
    const val FAILURE = 1
}

/**
 * Result of any [CliCommand] execution.
 * [code] return code
 * [exception] exception that occurred during the execution if any, otherwise - null
 */
data class ExecutionResult(val code: Int, val exception: Exception? = null)

/**
 * Builder for assembling info about command execution into an [ExecutionResult]
 */
internal class ExecutionResultBuilder {
    var code: Int = ReturnCode.OK
    var exception: Exception? = null

    fun buildResult(): ExecutionResult =
        ExecutionResult(code, exception)
}

/**
 * Wraps a function [block] that represents implementation of the command.
 * Allows setting [ExecutionResultBuilder] fields from inside of the [block].
 */
internal fun CliCommand.execution(block: ExecutionResultBuilder.() -> Unit): ExecutionResult =
    use {
        ExecutionResultBuilder()
            .apply(block)
            .buildResult()
    }

/**
 * Run a given [command].
 */
fun runCommand(command: CliCommand): ExecutionResult {
    val result = command.execute()
    if (result.code != ReturnCode.OK && result.exception != null) {
        println(result.exception.message)
    }
    return result
}

internal const val ANSI_RED = "\u001B[31m"
internal const val ANSI_RESET = "\u001B[0m"

/**
 * @return a receiver [String], but with a red color
 */
fun String.red() = "$ANSI_RED$this$ANSI_RESET"
