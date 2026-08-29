package com.example.devhub.data

import java.io.File

// Чистая утилита для работы с операционной системой.
// runConsoleCommand, которая дергает ProcessBuilder для запуска терминала или проводника

object CommandRunner {
    fun runConsoleCommand(projectPath: String, command: String) {
        val isWindows = System.getProperty("os.name").lowercase().contains("win")
        val commandParts = if (isWindows) {
            listOf("cmd.exe", "/c", command)
        } else {
            listOf("/bin/bash", "-c", command)
        }

        try {
            ProcessBuilder(commandParts)
                .directory(File(projectPath))
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()
        } catch (e: Exception) {
            println("Не удалось запустить команду: ${e.message}")
        }
    }
}
