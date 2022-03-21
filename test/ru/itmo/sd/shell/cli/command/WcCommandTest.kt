package ru.itmo.sd.shell.cli.command

class WcCommandTest : AbstractSimpleCommandTest() {

    override fun command(arguments: List<String>): CliSimpleCommand =
        WcCommand(arguments = arguments)
}