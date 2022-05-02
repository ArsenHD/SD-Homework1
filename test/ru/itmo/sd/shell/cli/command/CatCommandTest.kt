package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.ByteArrayOutputStream
import java.io.File

class CatCommandTest : AbstractCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testCat(fileNames: List<String>) {
        val outputStream = ByteArrayOutputStream()
        val cat = CommandFactoryHandler
            .getFactoryFor("cat")
            .createCommand(
                arguments = fileNames,
                outputStream = outputStream
            )

        assertResultSuccessful(
            command = cat,
            expected = expectedCat(fileNames),
        )
    }

    private fun expectedCat(fileNames: List<String>): String =
        fileNames.joinToString("") { File(it).readText() }

    companion object {
        @JvmStatic
        fun argumentProvider(): List<Arguments> {
            return listOf(
                arguments(listOf(TEST_FILE_1, TEST_FILE_2, TEST_FILE_3)),
                arguments(listOf(TEST_FILE_1)),
                arguments(listOf(TEST_FILE_3, TEST_FILE_4)),
                arguments(listOf(TEST_FILE_2, TEST_FILE_3)),
                arguments(listOf(TEST_FILE_1, TEST_FILE_3, TEST_FILE_5)),
                arguments(listOf(TEST_FILE_5, TEST_FILE_6, TEST_FILE_8)),
                arguments(listOf(TEST_FILE_1, TEST_FILE_3, TEST_FILE_7)),
                arguments(listOf(TEST_FILE_7, TEST_FILE_8, TEST_FILE_7))
            )
        }
    }
}
