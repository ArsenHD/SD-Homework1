package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PwdCommandTest : AbstractSimpleCommandTest() {

    @Test
    fun testPwd() {
        val pwd = command()
        val (code, _) = pwd.execute()
        val expected = "${System.getProperty("user.dir")}\n"
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(expected, outputStream.toString())
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        PwdCommand(arguments = arguments)
}