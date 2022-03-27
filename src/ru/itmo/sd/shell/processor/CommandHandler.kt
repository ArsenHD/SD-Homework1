package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.environment.Environment

internal class CommandHandler(private val environment: Environment) {

    fun substituteVariables(text: String): String =
        text.split(" ").joinToString(" ") { item ->
            when {
                item.startsWith("$") -> {
                    val name = item.drop(1)
                    environment[name] ?: item
                }
                else -> item
            }
        }
}