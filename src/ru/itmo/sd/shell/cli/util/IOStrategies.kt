package ru.itmo.sd.shell.cli.util

import java.io.Closeable
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.io.PrintWriter

sealed class InputStrategy : Closeable {
    abstract fun nextLine(): String?
}

sealed class OutputStrategy : Closeable {
    abstract fun write(text: String)
    abstract fun writeLine(text: String)
}

object StdInputStrategy : InputStrategy() {
    override fun nextLine(): String? = readLine()
    override fun close() = Unit
}

object StdOutputStrategy : OutputStrategy() {
    override fun write(text: String) {
        print(text)
    }

    override fun writeLine(text: String) {
        println(text)
    }

    override fun close() = Unit
}

class PipedInputStrategy(output: PipedOutputStream) : InputStrategy() {
    private val reader = PipedInputStream(output).bufferedReader()

    override fun nextLine(): String? = reader.readLine()

    override fun close() {
        reader.close()
    }
}

class PipedOutputStrategy : OutputStrategy() {
    val stream: PipedOutputStream = PipedOutputStream()
    private val writer = PrintWriter(stream)

    override fun write(text: String) {
        writer.println(text)
    }

    override fun writeLine(text: String) {
        writer.println(text)
    }

    override fun close() {
        writer.close()
    }
}
