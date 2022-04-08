package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class CdCommandTest : AbstractSimpleCommandTest() {

    @Test
    fun testCdWithoutArgs() {
        val oldWorkingDir = env.getPwd()
        val cd = command()
        val (code, _) = cd.execute(env)
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(oldWorkingDir, env.getPwd())
    }

    @Test
    fun testCdWithArgs() {
        val arg = File(".").absolutePath
        val cd = command(listOf(arg))
        val (code, _) = cd.execute(env)
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(arg, env.getPwd())
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        CdCommand(arguments = arguments)
}