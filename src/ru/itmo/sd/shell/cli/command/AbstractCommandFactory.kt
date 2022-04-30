package ru.itmo.sd.shell.cli.command

import java.io.InputStream
import java.io.OutputStream

object CommandFactoryHandler {
    fun getFactoryFor(commandName: String): AbstractCommandFactory =
        when (commandName) {
            "cat" -> CatCommandFactory()
            "echo" -> EchoCommandFactory()
            "exit" -> ExitCommandFactory()
            "grep" -> GrepCommandFactory()
            "pwd" -> PwdCommandFactory()
            "wc" -> WcCommandFactory()
            else -> ExternalCommandFactory(commandName)
        }
}

abstract class AbstractCommandFactory {
    abstract fun createCommand(
        arguments: List<String> = emptyList(),
        inputStream: InputStream = System.`in`,
        outputStream: OutputStream = System.out
    ): CliSimpleCommand
}

class CatCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = CatCommand(arguments, inputStream, outputStream)
}

class EchoCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = EchoCommand(arguments, inputStream, outputStream)
}

class ExitCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = ExitCommand(arguments, inputStream, outputStream)
}

class GrepCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = GrepCommand(arguments, inputStream, outputStream)
}

class PwdCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = PwdCommand(arguments, inputStream, outputStream)
}

class WcCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = WcCommand(arguments, inputStream, outputStream)
}

class ExternalCommandFactory(private val commandName: String) : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = ExternalCommand(arguments, commandName, inputStream, outputStream)
}
