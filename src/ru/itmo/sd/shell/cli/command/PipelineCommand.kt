package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.executorService
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.concurrent.Future

class PipelineCommand(
    override val inputStream: InputStream,
    override val outputStream: OutputStream,
    private val commands: List<CliSimpleCommand>
) : CliCommand() {
    // These streams are used to connect the user's I/O
    // with the inner piped I/O of the pipeline.
    // lhsConnection connects user's input with the pipeline
    // and rhsConnection connects the pipeline with user output.
    private val lhsConnection = PipedOutputStream()
    private val rhsConnection = PipedInputStream()

    init {
        for (i in 1..commands.lastIndex) {
            val lhsOutput = PipedOutputStream()
            val rhsInput = PipedInputStream()
            lhsOutput.connect(rhsInput)
            commands[i - 1].outputStream = lhsOutput
            commands[i].inputStream = rhsInput
        }
        // connect user's input to the piped commands' input
        val leftmostInput = PipedInputStream()
        lhsConnection.connect(leftmostInput)
        commands.first().inputStream = leftmostInput
        // connect commands' output to the user's output
        val rightmostOutput = PipedOutputStream()
        rightmostOutput.connect(rhsConnection)
        commands.last().outputStream = rightmostOutput
    }

    override fun execute(): ExecutionResult {
        // read user input to pipe so that it can be processed by commands
        val connectFromLeft = executorService.submit {
            lhsConnection.use { inputStream.copyTo(lhsConnection) }
        }

        // write pipe output to the user's output stream
        val connectFromRight = executorService.submit {
            rhsConnection.use { rhsConnection.copyTo(outputStream) }
        }

        // launch all commands in parallel
        val tasks = mutableListOf<Future<*>>()
        for (command in commands) {
            tasks += executorService.submit {
                command.use {
                    it.execute()
                }
            }
        }

        // wait until all commands finish execution
        tasks.forEach { task ->
            runCatching {
                task.get()
            }.onFailure { e ->
                throw e.cause ?: e
            }
        }

        // stop reading user input
        connectFromLeft.get()
        connectFromRight.get()

        return ExecutionResult.OK
    }

    override fun close() {
        inputStream.close()
        outputStream.close()
    }
}
