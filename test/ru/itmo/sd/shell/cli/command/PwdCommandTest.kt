package ru.itmo.sd.shell.cli.command

class PwdCommandTest : AbstractSimpleCommandTest() {

    override fun command(arguments: List<String>): CliSimpleCommand =
        PwdCommand(arguments = arguments)
}