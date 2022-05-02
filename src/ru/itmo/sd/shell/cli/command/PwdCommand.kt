package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.environment.Environment
import java.io.InputStream
import java.io.OutputStream

class PwdCommand(
    override val arguments: List<String>,
    override val environment: Environment = Environment(),
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand() {

    override val name: String = "pwd"

    override fun execute(): ExecutionResult {
        writeLine(System.getProperty("user.dir"))
        return ExecutionResult.OK
    }
}
