package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import java.io.InputStream
import java.io.OutputStream

class EchoCommand(
    override val arguments: List<String>,
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand() {

    override val name: String = "echo"

    override fun execute(): ExecutionResult {
        val result = arguments.joinToString(separator = " ")
        writeLine(result)
        return ExecutionResult.OK
    }
}
