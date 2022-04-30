package ru.itmo.sd.shell.parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.itmo.sd.shell.cli.command.CatCommand
import ru.itmo.sd.shell.cli.command.GrepCommand
import ru.itmo.sd.shell.environment.Environment
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CommandParserTest {
    @Test
    fun testParseSimpleCommand() {
        val inputStream = createInputStream("cat file1 file2 file3")
        val outputStream = ByteArrayOutputStream()
        val parser = CommandParser.getInstance(inputStream, outputStream, Environment())
        val command = parser.parse()

        Assertions.assertInstanceOf(CatCommand::class.java, command)
        command as CatCommand
        Assertions.assertEquals(3, command.arguments.size)
        Assertions.assertTrue(command.options.isEmpty())
    }

    @Test
    fun testParseCommandWithOptions() {
        val inputStream = createInputStream("grep -w [a-zA-Z][a-zA-Z0-9]* testResources/dir2/regex_file.txt")
        val outputStream = ByteArrayOutputStream()
        val parser = CommandParser.getInstance(inputStream, outputStream, Environment())
        val command = parser.parse()

        Assertions.assertInstanceOf(GrepCommand::class.java, command)
        command as GrepCommand
        Assertions.assertEquals(3, command.arguments.size)
        Assertions.assertEquals(1, command.options.size)
    }

    companion object {
        private fun createInputStream(text: String): InputStream =
            ByteArrayInputStream(text.toByteArray())
    }
}
