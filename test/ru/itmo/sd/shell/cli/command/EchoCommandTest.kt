package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class EchoCommandTest : AbstractSimpleCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testEcho(arguments: List<String>) {
        val echo = command(arguments)
        val expected = expectedEcho(arguments)
        val (code, output) = echo.execute()
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(expected, output)
    }

    private fun expectedEcho(arguments: List<String>): String =
        arguments.joinToString(separator = " ", postfix = "\n")

    companion object {
        @JvmStatic
        fun argumentProvider(): List<Arguments> {
            return listOf(
                Arguments.arguments(someWords.subList(0, 3)),
                Arguments.arguments(someWords.subList(0, 1)),
                Arguments.arguments(someWords.subList(2, 4)),
                Arguments.arguments(someWords.subList(1, 3)),
                Arguments.arguments(listOf(someWords[0], someWords[2], someWords[4]))
            )
        }

        private val someWords: List<String> = listOf(
            "abcde",
            "some_word",
            "this_is_a_very_long_word",
            "w0rd_w1th_num6er5",
            "anotherwordwithnounderscores",
        )
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        EchoCommand(arguments = arguments)
}