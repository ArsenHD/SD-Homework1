package ru.itmo.sd.shell.exception

class ExecutionFailureError(exitCode: Int) : Exception("Execution failed with exit code $exitCode")

abstract class SyntaxError(override val message: String?) : Exception()

class UnexpectedEofException : SyntaxError("unexpected end of file")

class UnmatchedParenthesesException : SyntaxError("unexpected EOF while looking for matching parentheses")

class ShellShutdownException : Exception()
