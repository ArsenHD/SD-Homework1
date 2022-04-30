package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.exception.ExecutionFailureError
import java.io.InputStream
import java.io.OutputStream

class ExternalCommand(
    override val arguments: List<String>,
    override val name: String,
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand() {

    override fun execute(): ExecutionResult {
        val process = ProcessBuilder("bash", "-c", name, *arguments.toTypedArray()).start()
        val returnCode = process.waitFor()
        if (returnCode != 0) {
            throw ExecutionFailureError(returnCode)
        }
        process.inputStream.use { input ->
            input.copyTo(outputStream)
        }
        return ExecutionResult.OK
    }
}
