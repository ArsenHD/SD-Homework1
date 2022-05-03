package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.exception.ShellShutdownException
import java.io.InputStream
import java.io.OutputStream

class ExitCommand(
    override val arguments: List<String>,
    override val environment: Environment = Environment(),
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand() {

    override val name: String = "exit"

    override fun execute(): ExecutionResult {
        writeLine("*** Exiting shell ***")
        // CommandProcessor will catch this exception and stop the application
        throw ShellShutdownException()
    }
}
