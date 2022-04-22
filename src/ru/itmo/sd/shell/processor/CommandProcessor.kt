package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.cli.command.*
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.exception.SyntaxError
import ru.itmo.sd.shell.parser.CommandParser

class CommandProcessor {
    private val environment = Environment()
    private val parser = CommandParser(
        inputStream = System.`in`,
        handler = CommandHandler(environment)
    )

    fun run() = try {
        var element = parser.parse()
        while (element != null) {
            process(element)
            element = parser.parse()
        }
        finish()
    } catch (e: SyntaxError) {
        println("shell: syntax error: ${e.message}")
    } catch (e: Exception) {
        println("shell: error: failed to execute command")
    }

    private fun process(cliElement: CliElement) {
        when (cliElement) {
            is CliVariableAssignment -> {
                val (name, value) = cliElement
                environment[name] = value
            }
            is CliEmptyLine -> Unit
            is CliCommand -> cliElement.execute()
        }
    }

    private fun finish() {
        println("*** Stopping shell ***")
    }
}
