package ru.itmo.sd.shell.exception

class ExecutionFailureError(exitCode: Int) : Exception("Execution failed with exit code $exitCode")

abstract class SyntaxError(override val message: String?) : Exception()

class UnexpectedEofException : SyntaxError("Unexpected end of file")

class UnmatchedQuoteException : SyntaxError("Input contains an unmatched quote character")

class ShellShutdownException : Exception()
