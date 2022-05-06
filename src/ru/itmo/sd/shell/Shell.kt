package ru.itmo.sd.shell

import ru.itmo.sd.shell.processor.CommandProcessor

fun main() {
    val processor = CommandProcessor(System.`in`, System.out)
    processor.run()
}
