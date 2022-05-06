package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import ru.itmo.sd.shell.cli.util.ExecutionResult

abstract class AbstractCommandTest {
    companion object {
        private const val TEST_RESOURCES = "testResources"

        const val TEST_FILE_1 = "$TEST_RESOURCES/dir1/abc.txt"
        const val TEST_FILE_2 = "$TEST_RESOURCES/dir1/my_file.txt"
        const val TEST_FILE_3 = "$TEST_RESOURCES/dir1/some_text.txt"
        const val TEST_FILE_4 = "$TEST_RESOURCES/dir2/draft.txt"
        const val TEST_FILE_5 = "$TEST_RESOURCES/dir2/file1.txt"
        const val TEST_FILE_6 = "$TEST_RESOURCES/dir2/file2.txt"
        const val TEST_FILE_7 = "$TEST_RESOURCES/dir2/regex_file.txt"
        const val TEST_FILE_8 = "$TEST_RESOURCES/some_file.txt"
    }

    fun assertResultSuccessful(
        command: CliCommand,
        expected: String
    ) {
        val status = command.execute()
        Assertions.assertEquals(ExecutionResult.OK, status)
        Assertions.assertEquals(expected, command.outputStream.toString())
    }
}
