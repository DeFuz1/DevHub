package com.example.devhub.data

import java.io.File

object CommandRunner {

    // Метод 1: Для открытия папки через встроенные утилиты ОС (например, explorer)
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

    // Метод 2: Новый метод специально для запуска IDE по кастомному пути пользователя
    fun openProjectInIde(idePath: String, projectPath: String) {
        try {
            // Запускаем указанный .exe файл и передаем ему путь к проекту как аргумент
            ProcessBuilder(idePath, projectPath).start()
        } catch (e: Exception) {
            println("Не удалось открыть проект по указанному пути IDE: ${e.message}")
        }
    }
}
