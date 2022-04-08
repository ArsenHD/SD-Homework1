package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import ru.itmo.sd.shell.cli.command.util.Utils
import ru.itmo.sd.shell.environment.Environment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream

abstract class AbstractCommandTest {
    protected lateinit var outputStream: ByteArrayOutputStream
    protected val env = Environment()
    @BeforeEach
    fun setupStreams() {
        outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
    }

    @AfterEach
    fun restoreStreams() {
        System.setOut(System.out)
    }

    companion object {
        val testFiles = Utils.testFiles

        val filesContent: Map<String, String> =
            testFiles.associateWith { File(it).readText() }
    }
}

abstract class AbstractSimpleCommandTest : AbstractCommandTest() {
    abstract fun command(arguments: List<String> = emptyList()): CliSimpleCommand
}
