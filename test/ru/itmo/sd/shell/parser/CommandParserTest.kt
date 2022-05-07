package ru.itmo.sd.shell.parser

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.itmo.sd.shell.cli.command.CatCommand
import ru.itmo.sd.shell.cli.command.CliEmptyLine
import ru.itmo.sd.shell.cli.command.CliVariableAssignment
import ru.itmo.sd.shell.cli.command.EchoCommand
import ru.itmo.sd.shell.cli.command.GrepCommand
import ru.itmo.sd.shell.cli.command.PipelineCommand
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
        Assertions.assertEquals(2, command.arguments.size)
        Assertions.assertEquals(1, command.options.size)
    }

    @Test
    fun testParseVariableDeclaration() {
        val inputStream = createInputStream("let x = 123", "echo \$x | cat")
        val outputStream = ByteArrayOutputStream()
        val parser = CommandParser.getInstance(inputStream, outputStream, Environment())

        val variableAssignment = parser.parse()
        Assertions.assertInstanceOf(CliVariableAssignment::class.java, variableAssignment)
        variableAssignment!!.execute()

        val pipelineCommand = parser.parse()
        Assertions.assertInstanceOf(PipelineCommand::class.java, pipelineCommand)
        pipelineCommand!!.execute()

        Assertions.assertEquals("123\n", outputStream.toString())
    }

    @Test
    fun testParseEmptyLine() {
        val inputStream = createInputStream("echo \'123\'", "", "echo \"456\"")
        val outputStream = ByteArrayOutputStream()
        val parser = CommandParser.getInstance(inputStream, outputStream, Environment())

        parser.parse().let {
            Assertions.assertInstanceOf(EchoCommand::class.java, it)
            it!!.execute()
        }

        parser.parse().let {
            Assertions.assertInstanceOf(CliEmptyLine::class.java, it)
        }

        parser.parse().let {
            Assertions.assertInstanceOf(EchoCommand::class.java, it)
            it!!.execute()
        }

        Assertions.assertEquals("123\n456\n", outputStream.toString())
    }

    companion object {
        private fun createInputStream(vararg lines: String): InputStream =
            lines.joinToString("\n")
                .toByteArray()
                .let { ByteArrayInputStream(it) }
    }
}
