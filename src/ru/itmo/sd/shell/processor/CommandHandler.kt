package ru.itmo.sd.shell.processor

import ru.itmo.sd.shell.environment.Environment

internal class CommandHandler(private val environment: Environment) {

    fun preprocessRawText(rawText: String): String {
        val inSingleQuotes = "'[^']*'".toRegex()
        val quotedFragments = inSingleQuotes.findAll(rawText)
            .toList()
            .map { it.range }
        if (quotedFragments.isEmpty()) {
            return substituteVariables(rawText).replace("\"", "")
        }
        return buildString {
            var pos = 0
            for (fragment in quotedFragments) {
                val (l, r) = fragment.first to fragment.last
                val toProcess = rawText.substring(pos until l)
                val toLeave = rawText.substring(l .. r)
                append(substituteVariables(toProcess))
                append(toLeave)
                pos = r + 1
            }
            if (pos < rawText.length) {
                val toProcess = rawText.substring(pos)
                append(substituteVariables(toProcess))
            }
        }.replace("[\"']".toRegex(), "")
    }

    private fun substituteVariables(text: String): String {
        val variableRegex = "\\$[a-zA-Z][a-zA-Z0-9]*".toRegex()
        return variableRegex.replace(text) {
            val name = it.value.drop(1)
            environment[name] ?: ""
        }
    }
}