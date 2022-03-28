package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import java.io.ByteArrayInputStream
import java.io.PipedInputStream

sealed interface CliElement

data class CliVariableAssignment(val name: String, val value: String) : CliElement

sealed class CliCommand : CliElement {
    abstract fun execute(input: String? = null): ExecutionResult
}

/**
 * All non-pipeline commands
 */
sealed class CliSimpleCommand : CliCommand() {
    abstract val name: String
    abstract val options: List<Option>
    abstract val arguments: List<String>

    override fun execute(input: String?): ExecutionResult {
        if (arguments.isNotEmpty()) {
            return processArguments()
        }
        if (input != null) {
            return processInput(input)
        }
        return processStdin()
    }

    open fun processArguments(): ExecutionResult = throw UnsupportedOperationException()

    open fun processInput(input: String): ExecutionResult = throw UnsupportedOperationException()

    open fun processStdin(): ExecutionResult = throw UnsupportedOperationException()
}

/**
 * All non-pipeline commands that are implemented without calling the actual system shell.
 * In other words, all simple commands except for [ExternalCommand]
 */
sealed class CliBuiltinCommand : CliSimpleCommand()
