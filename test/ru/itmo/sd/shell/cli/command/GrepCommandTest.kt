package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.itmo.sd.shell.cli.util.red

class GrepCommandTest : AbstractSimpleCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testGrep(expected: String, arguments: List<String>) {
        val grep = command(arguments)
        val (code, _) = grep.execute(env)
        assertEquals(0, code)
        assertEquals(expected, outputStream.toString())
    }

    companion object {
        @JvmStatic
        fun argumentProvider(): List<Arguments> {
            return listOf(
                // grep -i File testResources/dir1/abc.txt
                arguments(EXPECTED_1, listOf("-i", "File", "testResources/dir1/abc.txt")),
                // grep -i my testResources/dir1/my_file.txt
                arguments(EXPECTED_2, listOf("-i", "my", "testResources/dir1/my_file.txt")),
                // grep -i -w my testResources/dir1/my_file.txt
                arguments(EXPECTED_3, listOf("-i", "-w", "my", "testResources/dir1/my_file.txt")),
                // grep -A 3 pattern testResources/dir2/file2.txt
                arguments(EXPECTED_4, listOf("-A", "3", "pattern", "testResources/dir2/file2.txt")),
                // grep -w [a-zA-Z][a-zA-Z0-9]* testResources/dir2/regex_file.txt
                arguments(EXPECTED_5, listOf("-w", "[a-zA-Z][a-zA-Z0-9]*", "testResources/dir2/regex_file.txt"))
            )
        }

        private const val SEPARATOR_LINE = "____________________"
        private val LS = System.lineSeparator()

        private val EXPECTED_1 = "The contents of ${"file".red()} dir1/abc.txt$LS"
        private val EXPECTED_2 = "The contents of file dir1/${"my".red()}_file.txt$LS".repeat(10)
        private const val EXPECTED_3 = ""
        private val EXPECTED_4 = """
            $SEPARATOR_LINE
            First ${"pattern".red()} entry
            Some line
            Another line
            Yet another line
            $SEPARATOR_LINE
            There is another ${"pattern".red()} entry
            One more line$LS
        """.trimIndent()
        private val EXPECTED_5 = "123abc " + "a12".red() + " 4b 1qwerty " + "a".red() + " " + "abc".red() + LS
    }

    override fun command(arguments: List<String>) = GrepCommand(arguments = arguments)
}
