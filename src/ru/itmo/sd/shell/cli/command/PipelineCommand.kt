package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import java.io.InputStream
import java.io.OutputStream
import java.io.PipedInputStream
import java.io.PipedOutputStream
import kotlin.concurrent.thread

class PipelineCommand(
    val inputStream: InputStream,
    val outputStream: OutputStream,
    private val commands: List<CliSimpleCommand>
) : CliCommand() {
    init {
        for (i in 1..commands.lastIndex) {
            val output = commands[i - 1].outputStream
            val input = commands[i].inputStream
            output.connect(input)
        }
    }

    override fun execute(): ExecutionResult {
        // connect user input to the piped commands' input
        val outputLeft = PipedOutputStream()
        outputLeft.connect(commands.first().inputStream)
        val init = thread {
            outputLeft.use {
                while (!Thread.interrupted()) {
                    val n = inputStream.available()
                    if (n > 0) {
                        outputLeft.write(inputStream.readNBytes(n))
                    }
                }
            }
        }

        // launch all commands in parallel
        val threads = mutableListOf<Thread>()
        for (command in commands) {
            threads += thread {
                command.use {
                    it.execute()
                }
            }
        }

        // connect command output to the user's output
        val inputRight = PipedInputStream()
        commands.last().outputStream.connect(inputRight)
        val finish = thread {
            inputRight.use {
                while (!Thread.interrupted()) {
                    val n = inputRight.available()
                    if (n > 0) {
                        outputStream.write(inputRight.readNBytes(n))
                    }
                }
            }
        }

        // wait until all commands finish execution
        for (thread in threads) {
            thread.join()
        }

        // send interruption signals to stop reading user input
        init.interrupt()
        finish.interrupt()

        return ExecutionResult.OK
    }

    override fun close() {
        inputStream.close()
        outputStream.close()
    }
}
