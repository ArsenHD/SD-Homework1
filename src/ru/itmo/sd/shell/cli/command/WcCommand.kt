package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.execution
import java.io.File
import java.io.FileNotFoundException

class WcCommand(
    override val options: List<Option> = emptyList(),
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "wc"

    override fun processArguments(): ExecutionResult = processFiles()

    override fun processStdin(): ExecutionResult = execution {
        var line = readLine()
        val lines = sequence {
            while (line != null) {
                yield(line!!)
                line = readLine()
            }
        }
        val report = lines.processLines()
        writeLine(report)
    }

    private fun Sequence<String>.processLines(): Report {
        var newlines = 0
        var words = 0
        var bytes = 0

        for (line in this) {
            newlines++
            processLine(line).let {
                words += it.first
                bytes += it.second
            }
        }
        return Report(newlines, words, bytes)
    }

    private fun processLine(line: String): Pair<Int, Int> {
        val words = line
            .split(" ")
            .filter { it.isNotEmpty() }.size
        val bytes = line.toByteArray().size
        return words to bytes
    }

    private fun processFiles(): ExecutionResult = execution {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.exists() }
        errorFiles.forEach {
            writeLine("wc: ${it.name}: No such file or directory")
        }
        if (errorFiles.isNotEmpty()) {
            code = ReturnCode.FAILURE
            exception = FileNotFoundException(errorFiles.first().name)
            return@execution
        }

        var totalNewlines = 0
        var totalWords = 0
        var totalBytes = 0
        files.forEach {
            val (newlines, words, bytes) = processFile(it)
            totalNewlines += newlines
            totalWords += words
            totalBytes += bytes
            writeLine("$newlines $words $bytes ${it.name}")
        }
        if (files.size > 1) {
            writeLine("$totalNewlines $totalWords $totalBytes total")
        }
    }

    private fun processFile(file: File): Report =
        file.useLines {
            it.processLines()
        }

    data class Report(val newlines: Int, val words: Int, val bytes: Int) {
        override fun toString(): String = "$newlines $words $bytes"
    }
}
