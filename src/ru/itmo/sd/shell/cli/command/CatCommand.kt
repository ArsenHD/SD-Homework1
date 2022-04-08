package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.execution
import ru.itmo.sd.shell.environment.Environment
import java.io.File
import java.io.FileNotFoundException

class CatCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "cat"

    override fun execute(env: Environment): ExecutionResult {
        if (arguments.isNotEmpty()) {
            return processFiles()
        }
        return execution {
            var line = readLine()
            while (line != null) {
                writeLine(line)
                line = readLine()
            }
        }
    }

    private fun processFiles() = execution {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.isFile }

        errorFiles.forEach {
            writeLine("cat: ${it.name}: No such file or directory")
        }
        if (errorFiles.isNotEmpty()) {
            code = ReturnCode.FAILURE
            exception = FileNotFoundException(errorFiles.first().name)
            return@execution
        }

        files.forEach { writeFileContents(it) }
    }

    private fun writeFileContents(file: File) {
        file.bufferedReader().use { reader ->
            var char = reader.read()
            while (char != -1) {
                write(char.toChar())
                char = reader.read()
            }
        }
    }
}
