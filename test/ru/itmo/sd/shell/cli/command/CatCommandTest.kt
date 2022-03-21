package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource

class CatCommandTest : AbstractSimpleCommandTest() {

    @ParameterizedTest
    @MethodSource("argumentProvider")
    fun testCat(fileNames: List<String>) {
        val cat = command(fileNames)
        val expected = expectedCat(fileNames)
        val (code, output) = cat.execute()
        assertEquals(0, code)
        assertEquals(expected, output)
    }

    private fun expectedCat(fileNames: List<String>): String =
        fileNames.joinToString("") { filesContent[it]!! }

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

    override fun command(arguments: List<String>) = CatCommand(arguments = arguments)
}