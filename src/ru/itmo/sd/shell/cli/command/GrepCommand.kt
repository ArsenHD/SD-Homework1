package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.util.ExecutionResult
import ru.itmo.sd.shell.cli.util.red
import ru.itmo.sd.shell.environment.Environment
import java.io.File
import java.io.InputStream
import java.io.OutputStream

class GrepCommand(
    override val arguments: List<String>,
    override val environment: Environment = Environment(),
    override var inputStream: InputStream = System.`in`,
    override var outputStream: OutputStream = System.out
) : CliSimpleCommand() {
    override val optionsInfo = mapOf(
        IGNORE_CASE to 0,
        COMPLETE_WORDS to 0,
        PADDING to 1
    )

    private val ignoreCase: Boolean = IGNORE_CASE in options
    private val completeWords: Boolean = COMPLETE_WORDS in options
    private val padding: Int = options[PADDING]?.values?.firstOrNull()?.toInt() ?: 0

    private val regexOptions = if (ignoreCase) setOf(RegexOption.IGNORE_CASE) else emptySet()

    private val argumentsStartIdx: Int =
        when {
            options.isEmpty() -> 0
            else -> 1 + options.keys
                .associateWith { arguments.indexOf(it) }
                .map { (name, idx) -> idx + optionsInfo[name]!! }
                .maxOrNull()!!
        }

    private val pattern by lazy {
        if (completeWords)
            "${WORD_BORDER}${arguments[argumentsStartIdx]}$WORD_BORDER"
        else
            arguments[argumentsStartIdx]
    }

    private val regex: Regex = Regex(pattern, regexOptions)

    private val fileName: String? = arguments.getOrNull(argumentsStartIdx + 1)

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
