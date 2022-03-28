package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.cli.command.util.Utils
import java.io.File

abstract class AbstractCommandTest {
    companion object {
        val testFiles = Utils.testFiles
        val testDirs = Utils.testDirs

        val filesContent: Map<String, String> =
            testFiles.associateWith { File(it).readText() }
    }
}

abstract class AbstractSimpleCommandTest : AbstractCommandTest() {
    abstract fun command(arguments: List<String> = emptyList()): CliSimpleCommand
}
