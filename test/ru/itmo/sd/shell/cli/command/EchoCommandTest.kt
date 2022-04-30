package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream

class EchoCommandTest : AbstractCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testEcho(arguments: List<String>) {
        val outputStream = ByteArrayOutputStream()
        val echo = CommandFactoryHandler
            .getFactoryFor("echo")
            .createCommand(arguments, outputStream = outputStream)
        assertResultSuccessful(
            command = echo,
            expected = expectedEcho(arguments)
        )
    }

    private fun expectedEcho(arguments: List<String>): String =
        arguments.joinToString(separator = " ", postfix = "\n")

    companion object {
        @JvmStatic
        fun argumentProvider(): List<Arguments> {
            return listOf(
                Arguments.arguments(listOf(TEXT_1, TEXT_2, TEXT_3)),
                Arguments.arguments(listOf(TEXT_1)),
                Arguments.arguments(listOf(TEXT_2, TEXT_3)),
                Arguments.arguments(listOf(TEXT_1, TEXT_2, TEXT_4)),
                Arguments.arguments(listOf(TEXT_1, TEXT_3, TEXT_5))
            )
        }

        private const val TEXT_1 = "abcde"
        private const val TEXT_2 = "some_word"
        private const val TEXT_3 = "this_is_a_very_long_word"
        private const val TEXT_4 = "w0rd_w1th_num6er5"
        private const val TEXT_5 = "anotherwordwithnounderscores"
    }
}