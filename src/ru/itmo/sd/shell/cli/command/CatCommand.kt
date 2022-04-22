package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.execution
import java.io.File
import java.io.FileNotFoundException

class CatCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "cat"

    override fun execute(): ExecutionResult {
        if (arguments.isNotEmpty()) {
            return processFiles()
        }
        return execution {
            var char = read()
            while (char != -1) {
                write(char)
                char = read()
            }
        }
    }

    private fun processFiles() = execution {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.isFile }

        errorFiles.forEach {
            errorWriteLine("cat: ${it.name}: No such file or directory")
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
