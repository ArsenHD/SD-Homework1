package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.ReturnCode
import ru.itmo.sd.shell.cli.util.execution
import java.io.File
import java.io.FileNotFoundException

class WcCommand(
    override val arguments: List<String> = emptyList()
) : CliSimpleCommand() {

    override val name: String = "wc"

    override fun execute(): ExecutionResult {
        if (arguments.isNotEmpty()) {
            return processFiles()
        }
        return execution {
            val report = process(::read)
            writeLine(report)
        }
    }

    private fun processFiles(): ExecutionResult = execution {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.exists() }
        errorFiles.forEach {
            errorWriteLine("wc: ${it.name}: No such file or directory")
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
            it.bufferedReader().use { reader ->
                val (newlines, words, bytes) = process { reader.read() }//processFile(it)
                totalNewlines += newlines
                totalWords += words
                totalBytes += bytes
                writeLine("$newlines $words $bytes ${it.name}")
            }
        }
        if (files.size > 1) {
            writeLine("$totalNewlines $totalWords $totalBytes total")
        }
    }

    private fun process(nextChar: () -> Int): Report {
        var newlines = 0
        var words = 0
        var bytes = 0

        var char = nextChar()
        var isPrevWord = false
        while (char != -1) {
            bytes++
            when {
                char.toChar().isWhitespace() -> {
                    if (isPrevWord) {
                        isPrevWord = false
                        words++
                    }
                    if (char.toChar() == '\n') {
                        newlines++
                    }
                }
                else -> isPrevWord = true
            }
            char = nextChar()
        }
        if (isPrevWord) {
            words++
        }

        return Report(newlines, words, bytes)
    }

    data class Report(val newlines: Int, val words: Int, val bytes: Int) {
        override fun toString(): String = "$newlines $words $bytes"
    }
}
