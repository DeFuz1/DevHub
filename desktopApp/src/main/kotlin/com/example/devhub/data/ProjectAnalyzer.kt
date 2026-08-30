package com.example.devhub.data

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object ProjectAnalyzer {

    // Вызывает интерфейс для получения списка проектов
    fun getProjects(): List<LocalProject> {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")

        // 1. Берем сохраненные пути из репозитория конфигурации
        val paths = ProjectConfigRepository.getSavedPaths()

        // 2. Превращаем каждый путь в объект LocalProject с реальными данными с ПК
        return paths.map { path ->
            val folder = File(path)

            if (folder.exists() && folder.isDirectory) {
                LocalProject(
                    name = folder.name,
                    path = path,
                    technologies = detectFolderTechnologies(folder), // Реальный поиск технологий
                    dateAdded = dateFormat.format(Date(folder.lastModified())) // Реальная дата изменения папки
                )
            } else {
                // Если папку физически удалили с ПК, пока висел конфиг
                LocalProject(folder.name, path, "Папка не найдена на диске", "—")
            }
        }
    }

    // Внутренняя функция сканирования расширений файлов на жестком диске
    private fun detectFolderTechnologies(folder: File): String {
        val detectedTech = mutableSetOf<String>()

        // Список папок, в которые вообще не нужно заходить
        val ignoredDirs = setOf("build", "node_modules", ".git", ".gradle", ".idea", "bin", "out")

        try {
            folder.walkTopDown()
                .onEnter { dir ->
                    // Если имя папки в списке игнорируемых или скрытая — пропускаем всё её содержимое
                    dir.name !in ignoredDirs && !dir.name.startsWith(".")
                }
                .forEach { file ->
                    if (file.isFile) {
                        when (file.extension.lowercase()) {
                            "kt" -> {
                                detectedTech.add("Kotlin")

                                if ("Jetpack Compose" !in detectedTech && hasComposeImports(file)) {
                                    detectedTech.add("Jetpack Compose")
                                }
                            }
                            "html", "htm" -> detectedTech.add("HTML")
                            "css" -> detectedTech.add("CSS")
                            "js", "ts" -> detectedTech.add("JavaScript")
                        }
                    }
                }
        } catch (e: Exception) {
            println("Ошибка сканирования папки: ${e.message}")
        }

        return if (detectedTech.isEmpty()) "Не определено" else detectedTech.joinToString(", ")
    }

    // Быстрая проверка файла на наличие импортов Compose
    private fun hasComposeImports(file: File): Boolean {
        return try {
            file.useLines { lines ->
                // Читаем файл построчно, пока не найдем нужный импорт
                lines.any { line ->
                    line.startsWith("import androidx.compose.") ||
                            line.startsWith("import androidx.activity.compose.")
                }
            }
        } catch (e: Exception) {
            false
        }
    }
}
