package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExternalCommandTest : AbstractSimpleCommandTest() {

    @Test
    fun testExternalCommand() {
        val cmdName = "ls"
        val process = Runtime.getRuntime().exec(cmdName)
        val expectedCode = process.waitFor()
        val error = process.errorStream.reader().readText()
        val expected = process.inputStream.reader().readText()
        assertEquals(0, expectedCode)
        assertTrue(error.isEmpty())

        val (code, _) = command(listOf(cmdName)).execute(env)

        assertEquals(expectedCode, code)
        assertEquals(expected, outputStream.toString())
    }

    @Test
    fun testMissingCommand() {
        assertThrows<Exception> {
            command(listOf("abc")).execute(env)
        }
    }

    override fun command(arguments: List<String>): CliSimpleCommand {
        val name = arguments[0]
        val args = arguments.drop(1)
        return ExternalCommand(command = name, arguments = args)
    }
}