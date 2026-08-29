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

        try {
            folder.walkTopDown().forEach { file ->
                // Считаем технологии только в файлах, полностью игнорируя служебные тяжелые папки
                if (file.isFile && !file.absolutePath.contains("build") && !file.name.startsWith(".")) {
                    when (file.extension.lowercase()) {
                        "kt" -> detectedTech.add("Kotlin")
                        "html", "htm" -> detectedTech.add("HTML5")
                        "css" -> detectedTech.add("CSS3")
                        "js", "ts" -> detectedTech.add("JavaScript")
                    }
                }
            }
        } catch (e: Exception) {
            println("Ошибка сканирования папки: ${e.message}")
        }

        // Если кодовых файлов нет, пишем Не определено, иначе соединяем через запятую
        return if (detectedTech.isEmpty()) "Не определено" else detectedTech.joinToString(", ")
    }
}
