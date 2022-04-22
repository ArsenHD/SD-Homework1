package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.itmo.sd.shell.cli.util.ExecutionResult

class PipelineCommandTest : AbstractCommandTest() {

    @Test
    fun testPipeline() {
        val pwd = PwdCommand()
        val wc = WcCommand()

        val pipelineCmd = PipelineCommand(pwd, wc)
        val code = pipelineCmd.execute()
        val directory = System.getProperty("user.dir")
        val expected = "1 1 ${directory.length}\n"

        assertEquals(ExecutionResult.OK, code)
        assertEquals(expected, outputStream.toString())
    }
}
