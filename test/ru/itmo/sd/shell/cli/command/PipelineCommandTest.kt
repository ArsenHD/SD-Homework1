package ru.itmo.sd.shell.cli.command

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.itmo.sd.shell.cli.util.ExecutionResult

class PipelineCommandTest : AbstractCommandTest() {

    @Test
    fun testPipeline() {
        val pwd = mockk<PwdCommand>()
        val wc = WcCommand()
        every { pwd.execute() } returns ExecutionResult(0, DIR)

        val pipelineCmd = PipelineCommand(pwd, wc)
        val (code, output) = pipelineCmd.execute()
        val expected = "1 1 20\n"

        assertEquals(0, code)
        assertEquals(expected, output)
    }

    companion object {
        private const val DIR = "/home/arsen/SD/shell"
    }
}