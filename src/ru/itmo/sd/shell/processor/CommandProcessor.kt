package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.cli.util.executorService
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.exception.ShellShutdownException
import ru.itmo.sd.shell.exception.SyntaxError
import ru.itmo.sd.shell.parser.CommandParser
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

class CommandProcessor(inputStream: InputStream, outputStream: OutputStream) {
    private val environment = Environment()
    private val parser = CommandParser.getInstance(inputStream, outputStream, environment)
    private val writer = PrintWriter(outputStream.bufferedWriter(), true)

    fun run() {
        writer.println("*** The interpreter has been started ***")
        while (true) {
            try {
                var element = parser.parse()
                while (element != null) {
                    element.execute()
                    element = parser.parse()
                }
                break
            } catch (e: SyntaxError) {
                writer.println("shell: syntax error: ${e.message}")
            } catch (e: ShellShutdownException) {
                break
            } catch (e: Exception) {
                writer.println("shell: error: failed to execute command")
            }
        }
        finish()
    }

    private fun finish() {
        executorService.shutdown()
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)
        writer.println("*** Stopping shell ***")
        writer.close()
    }
}
