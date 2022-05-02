package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.cli.util.executorService
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.exception.ShellShutdownException
import ru.itmo.sd.shell.exception.SyntaxError
import ru.itmo.sd.shell.parser.CommandParser
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.TimeUnit

class CommandProcessor(inputStream: InputStream, outputStream: OutputStream) {
    private val environment = Environment()
    private val parser = CommandParser.getInstance(inputStream, outputStream, environment)

    fun run() {
        while (true) {
            try {
                var element = parser.parse()
                while (element != null) {
                    element.execute()
                    element = parser.parse()
                }
                finish()
                break
            } catch (e: SyntaxError) {
                println("shell: syntax error: ${e.message}")
            } catch (e: ShellShutdownException) {
                break
            } catch (e: Exception) {
                println("shell: error: failed to execute command")
            }
        }
    }

    private fun finish() {
        executorService.shutdown()
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)
        println("*** Stopping shell ***")
    }
}
