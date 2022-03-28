package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PwdCommandTest : AbstractSimpleCommandTest() {

    @Test
    fun testPwd() {
        val pwd = command()
        val (code, output) = pwd.execute()
        val expected = "${System.getProperty("user.dir")}\n"
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(expected, output)
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        PwdCommand(arguments = arguments)
}