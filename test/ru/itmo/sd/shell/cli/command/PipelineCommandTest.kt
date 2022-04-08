package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PipelineCommandTest : AbstractCommandTest() {

    @Test
    fun testPipeline() {
        val pwd = PwdCommand()
        val wc = WcCommand()

        val pipelineCmd = PipelineCommand(pwd, wc)
        val (code, _) = pipelineCmd.execute(env)
        val directory = System.getProperty("user.dir")
        val expected = "1 1 ${directory.length}\n"

        assertEquals(0, code)
        assertEquals(expected, outputStream.toString())
    }
}
