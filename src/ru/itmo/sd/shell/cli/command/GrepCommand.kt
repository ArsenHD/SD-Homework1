package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.Option
import ru.itmo.sd.shell.cli.util.red
import ru.itmo.sd.shell.environment.Environment
import ru.itmo.sd.shell.exception.IllegalCommandArgumentException
import ru.itmo.sd.shell.exception.IllegalOptionException
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class GrepCommand(
    _arguments: List<String>,
    override val environment: Environment = Environment(),
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand(_arguments) {
    override val optionsInfo = mapOf(
        IGNORE_CASE to 0,
        COMPLETE_WORDS to 0,
        PADDING to 1
    )

    private val ignoreCase: Boolean = IGNORE_CASE in options
    private val completeWords: Boolean = COMPLETE_WORDS in options
    private val padding: Int = options[PADDING]?.attributes?.get(0)?.toInt() ?: 0

    private val regexOptions = if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

    private val pattern by lazy {
        arguments.getOrNull(0)
            ?: throw IllegalCommandArgumentException("Pattern argument is missing")
        if (completeWords)
            "${WORD_BORDER}${arguments[0]}$WORD_BORDER"
        else
            arguments[0]
    }

    private val regex: Regex = Regex(pattern, regexOptions)

    private val fileName: String? = arguments.getOrNull(1)

    override val name: String = "grep"

    override fun execute(): ExecutionResult {
        if (fileName != null) {
            return processFile()
        }
        var line = readLine()
        while (line != null) {
            processLine(line)?.let { writeLine(it) }
            line = readLine()
        }
        return ExecutionResult.OK
    }

    override fun validateOption(option: Option) {
        if (option.name != PADDING) return
        val paddingValue = option.attributes.singleOrNull()
            ?: throw IllegalOptionException("One attribute was expected for '-A' option, but got: ${option.attributes.size}")
        val paddingIntValue = paddingValue.toIntOrNull()
            ?: throw IllegalOptionException("Attribute of '-A' option must be integer")
        paddingIntValue.takeIf { it >= 0 }
            ?: throw IllegalOptionException("Attribute '-A' must be non-negative")
    }

    private fun processLine(line: String): String? =
        line.takeIf { regex.containsMatchIn(it) }
            ?.replace(regex) { it.value.red() }

    private fun processFile(): ExecutionResult {
        val file = File(fileName!!)
        if (!file.isFile) {
            writeLine("grep: ${file.name}: No such file or directory")
            return ExecutionResult.FAILURE
        }

        var idx = 0
        var prevMatchIdx = -padding - 1
        file.forEachLine {
            val processed = processLine(it)
            if (processed != null) {
                if (idx - prevMatchIdx != 1) {
                    writeLine(SEPARATOR_LINE)
                }
                writeLine(processed)
                prevMatchIdx = idx
            } else if (idx - prevMatchIdx <= padding) {
                writeLine(it)
            }
            idx++
        }
        return ExecutionResult.OK
    }

    companion object {
        const val IGNORE_CASE = "-i"
        const val COMPLETE_WORDS = "-w"
        const val PADDING = "-A"

        const val WORD_BORDER = "\\b"
        const val SEPARATOR_LINE = "____________________"
    }
}
