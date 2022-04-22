package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import java.io.File

class CatCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "cat"

    override fun execute(): ExecutionResult {
        if (arguments.isNotEmpty()) {
            return processFiles()
        }
        var char = read()
        while (char != -1) {
            write(char)
            char = read()
        }
        return ExecutionResult.OK
    }

    private fun processFiles(): ExecutionResult {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.isFile }

        errorFiles.forEach {
            errorWriteLine("cat: ${it.name}: No such file or directory")
        }
        if (errorFiles.isNotEmpty()) {
            return ExecutionResult.FAILURE
        }

        files.forEach { writeFileContents(it) }
        return ExecutionResult.OK
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
