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

    abstract fun execute(): ExecutionResult

    fun readLine(): String? = inputStrategy.nextLine()

    fun read(): Int = inputStrategy.read()

    fun write(byte: Int) {
        outputStrategy.write(byte)
    }

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

    open fun connectTo(command: CliCommand) {
        val newOutputStrategy = PipedOutputStrategy()
        outputStrategy = newOutputStrategy
        command.inputStrategy = PipedInputStrategy(newOutputStrategy.stream)
    }
}

/**
 * All non-pipeline commands
 */
sealed class CliSimpleCommand : CliCommand() {
    abstract val name: String
    abstract val arguments: List<String>

    /**
     * Map from option name to a number of option parameters. Empty by default.
     *
     * For grep it will be `{("-i": 0), ("-w": 0), ("-A": 1)}`.
     *
     * In this example `-i` and `-w` are just flags, they have no parameters.
     * Whereas option `-A` has one parameter.
     */
    open val optionsInfo: Map<String, Int> = emptyMap()

    val options: Map<String, Option> by lazy {
        optionsInfo
            .filterKeys { it in arguments }
            .asSequence()
            .associate { (name, amount) ->
                val index = arguments.indexOf(name)
                val values = arguments.subList(index + 1, index + 1 + amount)
                name to Option(name, values)
            }
    }
}
