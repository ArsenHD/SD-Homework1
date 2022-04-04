package ru.itmo.sd.shell.environment

class Environment {
    private val variableByName: MutableMap<String, String> = System.getenv().toMutableMap()

    operator fun set(name: String, value: String) {
        variableByName[name] = value
    }

    operator fun get(name: String): String? = variableByName[name]

    fun getPwd(): String = variableByName["PWD"]!!

    fun setPwd(value: String) {
        variableByName["PWD"] = value
    }
}
