package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.execution
import java.io.File
import java.io.FileNotFoundException

class CatCommand(
    override val options: List<Option> = emptyList(),
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "cat"

    override fun processArguments(): ExecutionResult = processFiles()

    override fun processStdin(): ExecutionResult = execution {
        var line = readLine()
        while (line != null) {
            writeLine(line)
            line = readLine()
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

        for (file in files) {
            file.forEachLine { writeLine(it) }
        }
    }
}
