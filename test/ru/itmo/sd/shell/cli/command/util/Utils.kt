package ru.itmo.sd.shell.cli.command.util

import java.io.File

object Utils {
    private const val TEST_RESOURCES = "testResources"

    val testFiles: List<String> = File(TEST_RESOURCES)
        .walk()
        .filter { it.isFile }
        .map { it.path }
        .toList()

    val testDirs: List<String> = File(TEST_RESOURCES)
        .walk()
        .filter { it.isDirectory }
        .map { it.path }
        .toList()

//    val testFiles: List<String> by lazy {
//        File(TEST_RESOURCES).listFiles()!!.map { it.canonicalPath }
//    }
}