package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.InputStrategy
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.OutputStrategy
import ru.itmo.sd.shell.cli.util.PipedInputStrategy
import ru.itmo.sd.shell.cli.util.PipedOutputStrategy
import ru.itmo.sd.shell.cli.util.StdInputStrategy
import ru.itmo.sd.shell.cli.util.StdOutputStrategy
import java.io.Closeable

sealed interface CliElement

data class CliVariableAssignment(val name: String, val value: String) : CliElement

sealed class CliCommand : CliElement, Closeable {
    private var inputStrategy: InputStrategy = StdInputStrategy
    private var outputStrategy: OutputStrategy = StdOutputStrategy

    fun connectTo(command: CliCommand) {
        val newOutputStrategy = PipedOutputStrategy()
        outputStrategy = newOutputStrategy
        command.inputStrategy = PipedInputStrategy(newOutputStrategy.stream)
    }

    abstract fun execute(input: String? = null): ExecutionResult

    fun readLine(): String? = inputStrategy.nextLine()

    fun write(obj: Any) {
        outputStrategy.write("$obj")
    }

    fun writeLine(obj: Any) {
        outputStrategy.writeLine("$obj")
    }

    override fun close() {
        inputStrategy.close()
        outputStrategy.close()
    }
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
        return processStdin()
    }

    open fun processArguments(): ExecutionResult = throw UnsupportedOperationException()

    open fun processStdin(): ExecutionResult = throw UnsupportedOperationException()
}
