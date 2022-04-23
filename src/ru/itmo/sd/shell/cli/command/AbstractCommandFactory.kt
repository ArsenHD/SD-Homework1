package ru.itmo.sd.shell.cli.command

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
    abstract fun createCommand(arguments: List<String>): CliSimpleCommand
}

class CatCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = CatCommand(arguments)
}

class EchoCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = EchoCommand(arguments)
}

class ExitCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = ExitCommand(arguments)
}

class GrepCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = CatCommand(arguments)
}

class PwdCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = PwdCommand(arguments)
}

class WcCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = WcCommand(arguments)
}

class ExternalCommandFactory(private val commandName: String) : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliSimpleCommand = ExternalCommand(commandName, arguments)
}
