package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.itmo.sd.shell.exception.ExecutionFailureError
import java.io.ByteArrayOutputStream

class ExternalCommandTest : AbstractCommandTest() {

    @Test
    fun testExternalCommand() {
        val cmdName = "ls"
        val process = ProcessBuilder("bash", "-c", cmdName).start()//Runtime.getRuntime().exec(cmdName)
        val expectedCode = process.waitFor()
        val error = process.errorStream.reader().use { it.readText() }
        val expected = process.inputStream.reader().use { it.readText() }
        assertEquals(0, expectedCode)
        assertTrue(error.isEmpty())

        val outputStream = ByteArrayOutputStream()
        val command = CommandFactoryHandler
            .getFactoryFor(cmdName)
            .createCommand(outputStream = outputStream)

        assertResultSuccessful(
            command = command,
            expected = expected
        )
    }

    @Test
    fun testMissingCommand() {
        assertThrows<ExecutionFailureError> {
            CommandFactoryHandler
                .getFactoryFor("abc")
                .createCommand()
                .execute()
        }
    }
}
