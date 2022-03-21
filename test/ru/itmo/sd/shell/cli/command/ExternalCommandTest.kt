package ru.itmo.sd.shell.cli.command

class ExternalCommandTest : AbstractSimpleCommandTest() {

    override fun command(arguments: List<String>): CliSimpleCommand {
        val name = arguments[0]
        val args = arguments.drop(1)
        return ExternalCommand(command = name, arguments = args)
    }
}