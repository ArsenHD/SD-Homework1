package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.exception.ExecutionFailureError

class ExternalCommand(
    override val name: String,
    override val arguments: List<String>
) : CliSimpleCommand() {

    override fun execute(): ExecutionResult {
        val process = ProcessBuilder("bash", "-c", name, *arguments.toTypedArray()).start()
        val returnCode = process.waitFor()
        if (returnCode != 0) {
            throw ExecutionFailureError(returnCode)
        }
        process.inputStream.use { input ->
//            outputStream.use { output ->
//               input.copyTo(output)
//            }
            input.copyTo(outputStream)
        }
        return ExecutionResult.OK
    }
}
