package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.itmo.sd.shell.cli.util.ExecutionResult

class PwdCommandTest : AbstractSimpleCommandTest() {

    @Test
    fun testPwd() {
        val pwd = command()
        val code = pwd.execute()
        val expected = "${System.getProperty("user.dir")}\n"
        Assertions.assertEquals(ExecutionResult.OK, code)
        Assertions.assertEquals(expected, outputStream.toString())
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        PwdCommand(arguments = arguments)
}