package ru.itmo.sd.shell.cli.command

import ru.itmo.sd.shell.environment.Environment
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
        environment: Environment = Environment(),
        inputStream: InputStream = System.`in`,
        outputStream: OutputStream = System.out
    ): CliSimpleCommand
}

class CatCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = CatCommand(arguments, environment, inputStream, outputStream)
}

class EchoCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = EchoCommand(arguments, environment, inputStream, outputStream)
}

class ExitCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = ExitCommand(arguments, environment, inputStream, outputStream)
}

class GrepCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = GrepCommand(arguments, environment, inputStream, outputStream)
}

class PwdCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = PwdCommand(arguments, environment, inputStream, outputStream)
}

class WcCommandFactory : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = WcCommand(arguments, environment, inputStream, outputStream)
}

class ExternalCommandFactory(private val commandName: String) : AbstractCommandFactory() {
    override fun createCommand(
        arguments: List<String>,
        environment: Environment,
        inputStream: InputStream,
        outputStream: OutputStream
    ): CliSimpleCommand = ExternalCommand(arguments, environment, commandName, inputStream, outputStream)
}
