package ru.itmo.sd.shell.cli.util

import java.io.Closeable
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.io.PrintWriter

sealed class InputStrategy : Closeable {
    abstract fun nextLine(): String?
    abstract fun read(): Int
}

sealed class OutputStrategy : Closeable {
    private val errorOutputStream = System.err

    abstract fun write(byte: Int)

    abstract fun write(text: String)

    abstract fun writeLine(text: String)

    fun errorWrite(text: String) {
        errorOutputStream.print(text)
    }

    fun errorWriteLine(text: String) {
        errorOutputStream.println(text)
    }
}

object StdInputStrategy : InputStrategy() {
    override fun nextLine(): String? = readLine()
    override fun read(): Int = System.`in`.read()
    override fun close() = Unit
}

object StdOutputStrategy : OutputStrategy() {
    private val outputStream = System.out

    override fun write(byte: Int) {
        outputStream.write(byte)
    }

    override fun write(text: String) {
        outputStream.print(text)
    }

    override fun writeLine(text: String) {
        outputStream.println(text)
    }

    override fun close() = Unit
}

class PipedInputStrategy(output: PipedOutputStream) : InputStrategy() {
    private val reader = PipedInputStream(output).bufferedReader()

    override fun nextLine(): String? = reader.readLine()

    override fun read(): Int = reader.read()

    override fun close() {
        reader.close()
    }
}

class PipedOutputStrategy : OutputStrategy() {
    val outputStream: PipedOutputStream = PipedOutputStream()
    private val writer = PrintWriter(outputStream)

    override fun write(byte: Int) {
        writer.write(byte)
    }

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
