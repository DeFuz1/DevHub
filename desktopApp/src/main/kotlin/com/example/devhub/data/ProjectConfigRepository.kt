package com.example.devhub.data

import java.io.File

// Отвечает только за глобальный список папок и файл .devhub_config.json.
// Внутри него останутся функции addProjectPath, removeProjectPath, loadConfig и saveConfig

object ProjectConfigRepository {
    private val projectPaths = mutableListOf<String>()

    // 1. Путь к файлу настроек
    private val configFile = File(System.getProperty("user.home"), ".devhub_config.json")

    // Инициализация
    init {
        loadConfig()
    }
    fun addProjectPath(path: String) {
        if (path.isNotBlank() && !projectPaths.contains(path)) {
            projectPaths.add(path)
            saveConfig()
        }
    }
    fun getSavedPaths(): List<String> {
        return projectPaths.toList()
    }

    fun removeProjectPath(path: String) {
        if (projectPaths.remove(path)) {
            saveConfig()
        }
    }

    // 2. Функция сохранения списка путей в JSON-формат
    private fun saveConfig() {
        try {
            val jsonContent = projectPaths.joinToString(
                separator = ",\n  ",
                prefix = "[\n  ",
                postfix = "\n]"
            ) { path ->
                "\"${path.replace("\\", "\\\\")}\""
            }
            configFile.writeText(jsonContent)
        } catch (e: Exception) {
            println("Не удалось сохранить конфиг: ${e.message}")
        }
    }

    // 3. Функция загрузки списка путей из JSON-файла
    private fun loadConfig() {
        try {
            if (configFile.exists()) {
                val text = configFile.readText()
                // Парсинг JSON-массива строк
                val cleanedText = text
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\n", "")
                    .trim()

                if (cleanedText.isNotBlank()) {
                    val paths = cleanedText.split(",")
                    paths.forEach { rawPath ->
                        val cleanPath = rawPath.trim().removeSurrounding("\"").replace("\\\\", "\\")
                        if (cleanPath.isNotBlank() && File(cleanPath).exists()) {
                            projectPaths.add(cleanPath)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            println("Не удалось загрузить конфиг: ${e.message}")
        }
    }
}