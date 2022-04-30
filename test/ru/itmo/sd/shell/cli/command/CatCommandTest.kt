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
                arguments(testFiles.subList(0, 3)),
                arguments(testFiles.subList(0, 1)),
                arguments(testFiles.subList(2, 4)),
                arguments(testFiles.subList(1, 3)),
                arguments(listOf(testFiles[0], testFiles[2], testFiles[4]))
            )
        }
    }
}
