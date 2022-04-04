package ru.itmo.sd.shell.cli.command

import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class WcCommandTest : AbstractSimpleCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testWc(input: List<String>, expected: String) {
        mockkStatic(CONSOLE_KT)
        every { readLine() } returnsMany input andThen null
        val wc = command()
        val (code, _) = wc.execute(env)

        assertEquals(0, code)
        assertEquals(expected, outputStream.toString())
    }

    companion object {
        private const val CONSOLE_KT = "kotlin.io.ConsoleKt"
        private val LS = System.lineSeparator()

        private val inputs: List<List<String>> = listOf(
            "abc",
            "111001010${LS}010101${LS}001010",
            "some long string${LS}with newline${LS}",
            "1_*32m#42nq@45${LS}",
            "4124${LS}6126412${LS}141241${LS}"
        ).map { it.split(LS) }

        @JvmStatic
        fun argumentProvider(): List<Arguments> {
            return listOf(
                arguments(inputs[0], "1 1 3\n"),
                arguments(inputs[1], "3 3 21\n"),
                arguments(inputs[2], "3 5 28\n"),
                arguments(inputs[3], "2 1 14\n"),
                arguments(inputs[4], "4 3 17\n"),
            )
        }
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        WcCommand(arguments = arguments)
}