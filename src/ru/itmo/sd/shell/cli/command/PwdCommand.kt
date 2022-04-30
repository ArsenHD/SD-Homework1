package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import java.io.InputStream
import java.io.OutputStream

class PwdCommand(
    override val arguments: List<String>,
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand() {

    override val name: String = "pwd"

    override fun execute(): ExecutionResult {
        writeLine(System.getProperty("user.dir"))
        return ExecutionResult.OK
    }
}
