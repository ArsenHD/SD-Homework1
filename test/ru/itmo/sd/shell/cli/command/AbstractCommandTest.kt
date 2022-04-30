package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import ru.itmo.sd.shell.cli.command.util.Utils
import ru.itmo.sd.shell.cli.util.ExecutionResult

abstract class AbstractCommandTest {
    companion object {
        val testFiles = Utils.testFiles
    }

    fun assertResultSuccessful(
        command: CliCommand,
        expected: String,
    ) {
        val status = command.execute()
        Assertions.assertEquals(ExecutionResult.OK, status)
        Assertions.assertEquals(expected, command.outputStream.toString())
    }
}
