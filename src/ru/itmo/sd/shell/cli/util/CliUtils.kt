package ru.itmo.sd.shell.cli.util

import ru.itmo.sd.shell.cli.command.CliCommand
import java.util.concurrent.Executors

data class Option(val name: String, val values: List<String>) {
    override fun toString(): String = "-$name ${values.joinToString(" ")}"
}

/**
 * Result of any [CliCommand] execution.
 */
enum class ExecutionResult(val code: Int) {
    OK(0),
    FAILURE(1)
}

internal const val ANSI_RED = "\u001B[31m"
internal const val ANSI_RESET = "\u001B[0m"

/**
 * @return a receiver [String], but with a red color
 */
fun String.red() = "$ANSI_RED$this$ANSI_RESET"

internal val executorService = Executors.newCachedThreadPool()
