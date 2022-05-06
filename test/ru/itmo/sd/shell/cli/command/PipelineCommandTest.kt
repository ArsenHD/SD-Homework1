package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class PipelineCommandTest : AbstractCommandTest() {

    @Test
    fun testPipeline1() {
        val pwd = PwdCommand(emptyList())
        val wc = WcCommand(emptyList())

        val outputStream = ByteArrayOutputStream()
        val pipelineCmd = PipelineCommand(
            System.`in`,
            outputStream,
            listOf(pwd, wc)
        )
        val directory = System.getProperty("user.dir")

        assertResultSuccessful(
            command = pipelineCmd,
            expected = "1 1 ${directory.length + 1}\n"
        )
    }

    @Test
    fun testPipeline2() {
        val text = "some text"
        val echo = EchoCommand(listOf(text))
        val catCommands = List(4) { CatCommand(emptyList()) }

        val outputStream = ByteArrayOutputStream()
        val pipelineCmd = PipelineCommand(
            System.`in`,
            outputStream,
            // echo some text | cat | cat | cat | cat
            listOf(echo, catCommands[0], catCommands[1], catCommands[2], catCommands[3])
        )

        assertResultSuccessful(
            command = pipelineCmd,
            expected = "$text\n"
        )
    }

    @Test
    fun testPipeline3() {
        val text = "this is an example text"
        val echo = EchoCommand(listOf(text))
        val catCommands = List(3) { CatCommand(emptyList()) }
        val wc = WcCommand(emptyList())

        val outputStream = ByteArrayOutputStream()
        val pipelineCmd = PipelineCommand(
            System.`in`,
            outputStream,
            // echo this is an example text | cat | cat | cat | wc
            listOf(echo, catCommands[0], catCommands[1], catCommands[2], wc)
        )

        assertResultSuccessful(
            command = pipelineCmd,
            expected = "1 5 24\n"
        )
    }
}
