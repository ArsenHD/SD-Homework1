package ru.itmo.sd.shell.cli.command

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class PwdCommandTest : AbstractCommandTest() {

    @Test
    fun testPwd() {
        val outputStream = ByteArrayOutputStream()
        val pwd = CommandFactoryHandler
            .getFactoryFor("pwd")
            .createCommand(outputStream = outputStream)

        assertResultSuccessful(
            command = pwd,
            expected = "${System.getProperty("user.dir")}\n"
        )
    }
}
