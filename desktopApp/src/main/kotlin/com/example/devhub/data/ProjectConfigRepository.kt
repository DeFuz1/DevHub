package com.example.devhub.data

import java.io.File
import kotlin.text.clear

object ProjectConfigRepository {
    private val projectPaths = mutableListOf<String>()

    // Переменные для хранения путей к IDE в оперативной памяти
    private var customStudioPath: String = ""
    private var customIdeaPath: String = ""
    private var customVsCodePath: String = ""

    // Путь к единому файлу настроек приложения
    private val configFile = File(System.getProperty("user.home"), ".devhub_config.json")

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

    // МЕТОДЫ ДЛЯ СХРАНЕНИЯ И ПОЛУЧЕНИЯ ПУТЕЙ IDE

    fun saveCustomPaths(studio: String, idea: String, vsCode: String) {
        customStudioPath = studio
        customIdeaPath = idea
        customVsCodePath = vsCode
        saveConfig()
    }

    fun getCustomStudioPath(): String? = customStudioPath.ifBlank { null }
    fun getCustomIdeaPath(): String? = customIdeaPath.ifBlank { null }
    fun getCustomVsCodePath(): String? = customVsCodePath.ifBlank { null }
    fun getCustomVisualStudioPath(): String? = null

    // МОДЕРНИЗИРОВАННОЕ СОХРАНЕНИЕ В ЕДИНЫЙ JSON ОБЪЕКТ
    private fun saveConfig() {
        try {
            val jsonContent = buildString {
                appendLine("{")

                // 1. Записываем пути к IDE
                appendLine("  \"studioPath\": \"${customStudioPath.replace("\\", "\\\\")}\",")
                appendLine("  \"ideaPath\": \"${customIdeaPath.replace("\\", "\\\\")}\",")
                appendLine("  \"vsCodePath\": \"${customVsCodePath.replace("\\", "\\\\")}\",")

                // 2. Записываем массив проектов
                appendLine("  \"projects\": [")
                val projectsJson = projectPaths.joinToString(separator = ",\n    ") { path ->
                    "\"${path.replace("\\", "\\\\")}\""
                }
                append(if (projectsJson.isNotBlank()) "    $projectsJson\n" else "")
                appendLine("  ]")

                append("}")
            }
            configFile.writeText(jsonContent)
        } catch (e: Exception) {
            println("Не удалось сохранить конфиг: ${e.message}")
        }
    }

    // МОДЕРНИЗИРОВАННАЯ ЗАГРУЗКА ИЗ JSON ОБЪЕКТА
    private fun loadConfig() {
        try {
            if (!configFile.exists()) return
            val text = configFile.readText()

            // 1. Простой парсинг одиночных полей через регулярные выражения или поиск строк
            customStudioPath = parseJsonKey(text, "studioPath")
            customIdeaPath = parseJsonKey(text, "ideaPath")
            customVsCodePath = parseJsonKey(text, "vsCodePath")

            // 2. Вырезаем область массива "projects" и парсим папки
            val projectsSection = text.substringAfter("\"projects\": [", "").substringBefore("]")
            if (projectsSection.isNotBlank()) {
                projectsSection.split(",").forEach { rawPath ->
                    val cleanPath = rawPath.trim().removeSurrounding("\"").replace("\\\\", "\\")
                    if (cleanPath.isNotBlank() && File(cleanPath).exists()) {
                        projectPaths.add(cleanPath)
                    }
                }
            }
        } catch (e: Exception) {
            println("Не удалось загрузить конфиг: ${e.message}")
        }
    }

    // Хелпер для быстрого и безопасного извлечения строкового значения из сырого JSON
    private fun parseJsonKey(jsonText: String, key: String): String {
        val searchKey = "\"$key\":"
        if (!jsonText.contains(searchKey)) return ""
        return jsonText
            .substringAfter(searchKey)
            .substringBefore(",")
            .substringBefore("}")
            .trim()
            .removeSurrounding("\"")
            .replace("\\\\", "\\")
    }

    fun clearAllWorkspaceAndSettings() {
        // 1. Стираем всё из оперативной памяти репозитория
        projectPaths.clear()
        customStudioPath = ""
        customIdeaPath = ""
        customVsCodePath = ""

        // 2. Физически удаляем файл конфигурации с жесткого диска
        try {
            if (configFile.exists()) {
                configFile.delete()
                println("Все настройки и кеш DevHub успешно удалены с диска.")
            }
        } catch (e: Exception) {
            println("Ошибка при удалении файла конфигурации: ${e.message}")
        }
    }
}