package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult

class ExternalCommand(
    override val name: String,
    override val arguments: List<String>
) : CliSimpleCommand() {

    override fun execute(): ExecutionResult {
        val process = ProcessBuilder(name, *arguments.toTypedArray()).start()
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
