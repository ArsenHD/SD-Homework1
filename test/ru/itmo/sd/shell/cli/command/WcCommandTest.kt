package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class WcCommandTest : AbstractCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testWc(input: String, expected: String) {
        val inputStream = ByteArrayInputStream(input.toByteArray())
        val outputStream = ByteArrayOutputStream()

        val wc = CommandFactoryHandler
            .getFactoryFor("wc")
            .createCommand(
                inputStream = inputStream,
                outputStream = outputStream
            )

        assertResultSuccessful(
            command = wc,
            expected = expected
        )
    }

    companion object {
        private val LS = System.lineSeparator()
        private val EOT = 4.toChar()

        private val inputs: List<String> = listOf(
            "abc",
            "111001010${LS}010101${LS}001010",
            "some long string${LS}with newline${LS}",
            "1_*32m#42nq@45${LS}",
            "4124${LS}6126412${LS}141241${LS}"
        ).map { (it + EOT) }

        @JvmStatic
        fun argumentProvider(): List<Arguments> {
            return listOf(
                arguments(inputs[0], "0 1 3\n"),
                arguments(inputs[1], "2 3 23\n"),
                arguments(inputs[2], "2 5 30\n"),
                arguments(inputs[3], "1 1 15\n"),
                arguments(inputs[4], "3 3 20\n"),
            )
        }
    }
}
