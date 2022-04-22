package ru.itmo.sd.shell.cli.command.util

import ru.itmo.sd.shell.cli.command.CatCommand
import ru.itmo.sd.shell.cli.command.CliCommand
import ru.itmo.sd.shell.cli.command.EchoCommand
import ru.itmo.sd.shell.cli.command.ExitCommand
import ru.itmo.sd.shell.cli.command.ExternalCommand
import ru.itmo.sd.shell.cli.command.PwdCommand
import ru.itmo.sd.shell.cli.command.WcCommand

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
    abstract fun createCommand(arguments: List<String>): CliCommand
}

class CatCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = CatCommand(arguments)
}

class EchoCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = EchoCommand(arguments)
}

class ExitCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = ExitCommand(arguments)
}

class GrepCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = CatCommand(arguments)
}

class PwdCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = PwdCommand(arguments)
}

class WcCommandFactory : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = WcCommand(arguments)
}

class ExternalCommandFactory(private val commandName: String) : AbstractCommandFactory() {
    override fun createCommand(arguments: List<String>): CliCommand = ExternalCommand(commandName, arguments)
}

