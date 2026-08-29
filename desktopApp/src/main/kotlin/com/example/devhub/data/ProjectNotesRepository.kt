package com.example.devhub.data

import java.io.File

object ProjectNotesRepository {
    fun saveNote(projectPath: String, text: String) {
        try {
            File(projectPath, "devhub_notes.md").writeText(text)
        } catch (e: Exception) {
            println("Ошибка сохранения файла: ${e.message}")
        }
    }

    fun loadNote(projectPath: String): String {
        val file = File(projectPath, "devhub_notes.md")
        return if (file.exists()) file.readText() else ""
    }
}
