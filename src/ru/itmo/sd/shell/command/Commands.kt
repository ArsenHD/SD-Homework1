package ru.itmo.sd.shell.command

import org.apache.commons.io.CopyUtils
import org.apache.commons.io.IOUtils
import java.io.*
import kotlin.system.exitProcess

sealed interface Command

data class Option(val value: String)

data class VariableAssignment(val name: String, val value: String) : Command

sealed class CliCommand : Command {
    abstract fun execute(): String

    var inputStream: InputStream = System.`in`
    var outputStream: PrintStream = System.out

    protected fun readLine(): String? =
        runCatching {
            inputStream.bufferedReader().readLine()
        }.getOrNull()

    protected fun println(text: String) {
        outputStream.println(text)
    }

    val arguments: MutableList<String> = mutableListOf()
    val options: MutableList<Option> = mutableListOf()
}

class PipelineCommand(val left: CliCommand, val right: CliCommand) : CliCommand() {
    override fun execute(): String {
        left.execute()
        right.execute()
    }
}

class ExternalCommand(private val command: String) : CliCommand() {
    override fun execute(): String {
        val process = Runtime.getRuntime().exec("$this")
        process.waitFor()
        val error = process.errorStream.reader().readText()
        if (error.isNotEmpty()) {
            return "Error: $error"
        }
        return process.inputStream.reader().readText()
    }

    override fun toString(): String {
        val opts = options.joinToString(" ") { "-${it.value}" }
        val args = arguments.joinToString(" ")
        return "$command $opts $args"
    }
}

class EchoCommand : CliCommand() {
    override fun execute(): String = arguments.joinToString(" ", postfix = System.lineSeparator())
}

class CatCommand : CliCommand() {
    override fun execute(): String = buildString {
        return if (arguments.isEmpty()) {
            processStdin()
        } else {
            processFiles()
        }
    }

    private fun processFiles() = buildString {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.exists() }
        errorFiles.forEach {
            appendLine("cat: ${it.name}: No such file or directory")
        }
        if (errorFiles.isNotEmpty()) {
            return@buildString
        }

        files.map { it.readLines() }
            .forEach { append(it) }
    }

    private fun processStdin(): String {
        var line = readLine()
        while (line != null) {
            println(line)
            line = readLine()
        }
        return ""
    }
}

class WcCommand : CliCommand() {
    override fun execute(): String {
        return if (arguments.isEmpty()) {
            processStdin()
        } else {
            processFiles()
        }
    }

    private fun processFiles(): String = buildString {
        val files = arguments.map { File(it) }
        val errorFiles = files.filter { !it.exists() }
        errorFiles.forEach {
            appendLine("wc: ${it.name}: No such file or directory")
            return@buildString
        }
        if (errorFiles.isNotEmpty()) {
            return@buildString
        }

        var totalNewlines = 0
        var totalWords = 0
        var totalBytes = 0
        files.forEach {
            val (newlines, words, bytes) = processFile(it)
            totalNewlines += newlines
            totalWords += words
            totalBytes += bytes
            appendLine("$newlines $words $bytes ${it.name}")
        }
        if (files.size > 1) {
            appendLine("$totalNewlines $totalWords $totalBytes total")
        }
    }

    private fun processFile(file: File): Triple<Int, Int, Int> =
        file.useLines {
            it.processLines()
        }

    private fun processStdin(): String {
        var line = readLine()
        val lines = sequence {
            while (line != null) {
                yield(line!!)
                line = readLine()
            }
        }
        val (newlines, words, bytes) = lines.processLines()
        return "$newlines $words $bytes"
    }

    private fun Sequence<String>.processLines(): Triple<Int, Int, Int> {
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
        return Triple(newlines, words, bytes)
    }

    private fun processLine(line: String): Pair<Int, Int> {
        val words = line.split(" ").size
        val bytes = line.toByteArray().size
        return words to bytes
    }
}

class PwdCommand : CliCommand() {
    override fun execute(): String = System.getProperty("user.dir")
}

class ExitCommand : CliCommand() {
    override fun execute(): String {
        println("*** Exiting shell ***")
        exitProcess(0)
    }
}
