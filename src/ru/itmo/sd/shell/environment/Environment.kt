package ru.itmo.sd.shell.environment

class Environment {
    private val variableByName: MutableMap<String, String> = mutableMapOf()

    operator fun set(name: String, value: String) {
        variableByName[name] = value
    }

    fun get(name: String): String? = variableByName[name]
}
