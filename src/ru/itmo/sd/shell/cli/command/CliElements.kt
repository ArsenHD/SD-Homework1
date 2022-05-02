package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.environment.Environment
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter

interface CliElement {
    fun execute(): ExecutionResult
}

interface CliStatefulElement : CliElement {
    val environment: Environment
}

data class CliVariableAssignment(
    override val environment: Environment,
    val name: String,
    val value: String
) : CliStatefulElement {
    override fun execute(): ExecutionResult {
        environment[name] = value
        return ExecutionResult.OK
    }
}

/**
 * This is and element being created when a user enters an empty line.
 * It represents no action.
 */
object CliEmptyLine : CliElement {
    override fun execute(): ExecutionResult = ExecutionResult.OK
}

sealed class CliCommand : CliStatefulElement {
    abstract val inputStream: InputStream
    abstract val outputStream: OutputStream
}

/**
 * All non-pipeline commands
 */
abstract class CliSimpleCommand : CliCommand(), Closeable {
    open val arguments: List<String> = emptyList()

    abstract val name: String

    abstract override var inputStream: InputStream
    abstract override var outputStream: OutputStream

    private val reader by lazy { inputStream.bufferedReader() }
    private val writer by lazy { PrintWriter(outputStream.writer(), true) }

    fun readLine(): String? = reader.readLine()

    fun read(): Int = reader.read()

    fun write(byte: Int) {
        writer.write(byte)
        writer.flush()
    }

    fun write(obj: Any) {
        writer.write("$obj")
        writer.flush()
    }

    fun writeLine(obj: Any) {
        writer.println("$obj")
        writer.flush()
    }

    override fun close() {
        reader.close()
        writer.close()
    }

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
