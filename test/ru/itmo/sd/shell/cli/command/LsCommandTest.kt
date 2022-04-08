package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class LsCommandTest : AbstractSimpleCommandTest() {

    @Test
    fun testLsWithoutArgs() {
        val ls = command()
        val (code, _) = ls.execute(env)
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(File(env.getPwd()).listFiles()?.joinToString("\n") { it.name }+"\n", outputStream.toString())
    }

    @Test
    fun testLsWithArgs() {
        val ls = command(listOf("."))
        val (code, _) = ls.execute(env)
        Assertions.assertEquals(0, code)
        Assertions.assertEquals(File(".").listFiles()?.joinToString("\n") { it.name }+"\n", outputStream.toString())
    }

    override fun command(arguments: List<String>): CliSimpleCommand =
        LsCommand(arguments = arguments)
}