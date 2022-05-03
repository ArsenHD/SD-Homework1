package ru.itmo.sd.shell.environment

class Environment(var currentDirectory: String = System.getProperty("user.dir")) {
    private val variableByName: MutableMap<String, String> = mutableMapOf()

    operator fun set(name: String, value: String) {
        variableByName[name] = value
    }

    operator fun get(name: String): String? = variableByName[name]

    operator fun contains(name: String): Boolean = name in variableByName
}
